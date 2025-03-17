package com.evotek.iam.application.service.impl.command;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.evo.common.dto.event.SendNotificationEvent;
import com.evo.common.enums.Channel;
import com.evo.common.enums.KafkaTopic;
import com.evo.common.enums.TemplateCode;
import com.evo.common.support.SecurityContextUtils;
import com.evotek.iam.application.configuration.TokenProvider;
import com.evotek.iam.application.dto.request.LoginRequest;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.application.service.AuthServiceCommand;
import com.evotek.iam.domain.User;
import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;
import com.evotek.iam.domain.command.WriteLogCmd;
import com.evotek.iam.domain.repository.UserDomainRepository;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakService;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;
import com.evotek.iam.infrastructure.support.exception.ErrorNormalizer;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component("keycloakAuthCommandService")
@RequiredArgsConstructor
public class KeycloakAuthCommandServiceImpl implements AuthServiceCommand {
    private final KeycloakService keycloakService;
    private final ErrorNormalizer errorNormalizer;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final UserDomainRepository userDomainRepository;
    private final CommandMapper commandMapper;
    private final KafkaTemplate<String, SendNotificationEvent> kafkaTemplate;
    private final TokenProvider tokenProvider;

    @Value("${jwt.valid-duration}")
    private long validDuration;

    @Value("${jwt.refreshable-duration}")
    private long refreshableDuration;

    @Override
    public TokenDTO authenticate(LoginRequest loginRequest) {
        try {
            TokenDTO tokenDTO = keycloakService.authenticate(loginRequest);

            User user = userDomainRepository.getByUsername(loginRequest.getUsername());
            WriteLogCmd logCmd = commandMapper.from("Login");
            UserActivityLog userActivityLog = new UserActivityLog(logCmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);

            Map<String, Object> params = SecurityContextUtils.getSecurityContextMap();
            params.put("username", loginRequest.getUsername());
            SendNotificationEvent mailAlert = SendNotificationEvent.builder()
                    .channel(Channel.EMAIL.name())
                    .recipient(user.getEmail())
                    .templateCode(TemplateCode.LOGIN_ALERT)
                    .param(params)
                    .build();
            kafkaTemplate.send(KafkaTopic.SEND_NOTIFICATION_GROUP.getTopicName(), mailAlert);
            return tokenDTO;
        } catch (FeignException exception) {
            throw errorNormalizer.handleKeyCloakException(exception);
        }
    }

    @Override
    public void logoutIam(HttpServletRequest request, String refreshToken) {
        try {
            String token = "";
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
            SignedJWT signedJWT = SignedJWT.parse(token);
            String accessJit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            redisTemplate.opsForValue().set(accessJit, expiryTime, 3600, TimeUnit.MILLISECONDS);
            keycloakService.logout(authorizationHeader, refreshToken);

            String username = signedJWT.getJWTClaimsSet().getStringClaim("preferred_username");
            User user = userDomainRepository.getByUsername(username);

            WriteLogCmd cmd = commandMapper.from("Logout");
            UserActivityLog userActivityLog = new UserActivityLog(cmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);
        } catch (FeignException exception) {
            throw errorNormalizer.handleKeyCloakException(exception);
        } catch (ParseException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public TokenDTO refresh(String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }

    @Override
    public void requestPasswordReset(String username, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            User user = userDomainRepository.getByUsername(username);
            String token = generateToken(user, true, false);
            String resetUrl = "http://127.0.0.1:5500/resetPassword.html?token=" + token;
            Map<String, Object> params = SecurityContextUtils.getSecurityContextMap();
            params.put("resetUrl", resetUrl);
            params.put("username", username);
            SendNotificationEvent mailAlert = SendNotificationEvent.builder()
                    .channel(Channel.EMAIL.name())
                    .recipient(user.getEmail())
                    .templateCode(TemplateCode.REQUEST_CHANGE_PASSWORD)
                    .param(params)
                    .build();
            kafkaTemplate.send(KafkaTopic.SEND_NOTIFICATION_GROUP.getTopicName(), mailAlert);
        } catch (Exception ex) {
            throw new AuthException(AuthErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void resetPassword(String token, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            SignedJWT signedJWT = verifyToken(token);
            String username = signedJWT.getJWTClaimsSet().getSubject();
            User user = userDomainRepository.getByUsername(username);
            user.changePassword(passwordEncoder.encode(resetKeycloakPasswordCmd.getValue()));

            keycloakService.resetPassword(user.getProviderId(), resetKeycloakPasswordCmd);

            WriteLogCmd cmd = commandMapper.from("Change password");
            UserActivityLog userActivityLog = new UserActivityLog(cmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);

            Map<String, Object> params = SecurityContextUtils.getSecurityContextMap();
            SendNotificationEvent mailAlert = SendNotificationEvent.builder()
                    .channel(Channel.EMAIL.name())
                    .recipient(user.getEmail())
                    .templateCode(TemplateCode.PASSWORD_CHANGE_ALERT)
                    .param(params)
                    .build();
            kafkaTemplate.send(KafkaTopic.SEND_NOTIFICATION_GROUP.getTopicName(), mailAlert);
        } catch (Exception ex) {
            throw new AuthException(AuthErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public String generateToken(User user, boolean isForgotPassword, boolean isRefresh) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("evotek.iam.com")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getSelfUserID());
        if (!isRefresh) {
            Date expirationTime = new Date(
                    Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli());
            if (isForgotPassword) {
                expirationTime =
                        new Date(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli());
            }

            claimsBuilder.expirationTime(expirationTime);
        }
        JWTClaimsSet jwtClaimsSet = claimsBuilder.build();
        JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));
        RSASSASigner signer = new RSASSASigner(tokenProvider.getKeyPair().getPrivate());
        try {
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AuthException(AuthErrorCode.CAN_NOT_CREATE_TOKEN);
        }
    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier =
                    new RSASSAVerifier((RSAPublicKey) tokenProvider.getKeyPair().getPublic());
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isRefresh = signedJWT.getJWTClaimsSet().getExpirationTime() == null;
            Date expiryTime = isRefresh
                    ? new Date(signedJWT
                            .getJWTClaimsSet()
                            .getIssueTime()
                            .toInstant()
                            .plus(refreshableDuration, ChronoUnit.SECONDS)
                            .toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);
            if (!(verified && expiryTime.after(new Date()))) throw new AuthException(AuthErrorCode.UNAUTHENTICATED);
            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}
