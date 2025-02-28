package com.evotek.iam.application.service.impl.command;

import com.evotek.iam.application.dto.request.LoginRequest;
import com.evotek.iam.application.dto.request.VerifyOtpRequest;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;
import com.evotek.iam.application.dto.request.identityKeycloak.GetTokenRequest;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.domain.User;
import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.command.LoginCmd;
import com.evotek.iam.domain.command.VerifyOtpCmd;
import com.evotek.iam.domain.command.WriteLogCmd;
import com.evotek.iam.domain.repository.UserDomainRepository;
import com.evotek.iam.infrastructure.adapter.email.EmailService;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;
import com.evotek.iam.application.service.AuthServiceCommand;
import com.evotek.iam.infrastructure.support.exception.ErrorNormalizer;
import com.evotek.iam.application.configuration.TokenProvider;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakIdentityClient;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component("self_idp_auth_command_service")
@RequiredArgsConstructor
@Slf4j
public class SelfIDPAuthCommandServiceImpl implements AuthServiceCommand {
    private final UserDomainRepository userDomainRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ErrorNormalizer errorNormalizer;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;
    private final KeycloakIdentityClient keycloakIdentityClient;
    private final CommandMapper commandMapper;
    private final String INVALID_REFRESH_TOKEN_CACHE = "invalid-refresh-token";
    private final String INVALID_TOKEN_CACHE = "invalid-access-token";

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;
    @Value("${idp.client-id}")
    private String clientId;
    @Value("${idp.client-secret}")
    private String clientSecret;


    public String generateToken(User user, boolean isForgotPassword, boolean isRefresh) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("evotek.iam.com")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getSelfUserID());
        if (!isRefresh) {
            Date expirationTime = new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli());
            if(isForgotPassword){
                expirationTime = new Date(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli());
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
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenDTO authenticate(LoginRequest loginRequest) {
        LoginCmd loginCmd = commandMapper.from(loginRequest);
        User user = userDomainRepository
                .getByUsername((loginCmd.getUsername()));
        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!authenticated) throw new AuthException(AuthErrorCode.UNAUTHENTICATED);
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        redisTemplate.opsForValue().set(String.valueOf(otp), user.getUsername(), 300, TimeUnit.SECONDS);
        emailService.sendMailOtp(user.getEmail(), String.valueOf(otp));
        return null;
    }

    public TokenDTO verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        VerifyOtpCmd cmd = commandMapper.from(verifyOtpRequest);
        if (!redisTemplate.hasKey(cmd.getOtp())) {
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED);
        }
        String otpStored = redisTemplate.opsForValue().get(cmd.getOtp());
        if (otpStored != null && !otpStored.equals(cmd.getUsername())){
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED);
        }
        User user = userDomainRepository
                .getByUsername((cmd.getUsername()));
        redisTemplate.delete(cmd.getOtp());
        var accessToken = generateToken(user, false, false);
        var refreshToken = generateToken(user, false, true);
        WriteLogCmd logCmd = commandMapper.from("Login");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        user.setUserActivityLog(userActivityLog);
        userDomainRepository.save(user);
        return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public void logoutIam(HttpServletRequest request, String refreshToken) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            var signedJWT = verifyToken(token);
            var access_expiryTimeMillis = signedJWT.getJWTClaimsSet().getExpirationTime().getTime();
            long currentTimeMillis = System.currentTimeMillis();
            long access_remainingTimeMillis = access_expiryTimeMillis - currentTimeMillis;
            long refresh_remainingTimeMillis = currentTimeMillis + REFRESHABLE_DURATION * 1000;
            addAccessTokenToBlacklist(token, access_remainingTimeMillis);
            addRefreshTokenToBlacklist(token, refresh_remainingTimeMillis);
            UUID userId = UUID.fromString(signedJWT.getJWTClaimsSet().getClaim("userId").toString());
            User user = userDomainRepository.getById(userId);
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
        try {
            var signedJWT = verifyToken(refreshToken);
            String username = signedJWT.getJWTClaimsSet().getSubject();
            User user = userDomainRepository.getByUsername(username);
            var accessToken = generateToken(user, false, false);
            return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        } catch (ParseException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public void requestPasswordReset(String username, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            User user = userDomainRepository.getByUsername(username);
            String token = generateToken(user, true, false);
            String resetLink = "http://127.0.0.1:5500/resetPassword.html?token=" + token;
            emailService.sendMailForResetPassWord(user.getEmail(), resetLink);
        } catch (Exception ex) {
            throw new AuthException(AuthErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) tokenProvider.getKeyPair().getPublic());
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isRefresh = signedJWT.getJWTClaimsSet().getExpirationTime() == null;
            Date expiryTime = isRefresh
                    ? new Date(signedJWT
                    .getJWTClaimsSet()
                    .getIssueTime()
                    .toInstant()
                    .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                    .toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);
            if (!(verified && expiryTime.after(new Date()))) throw new AuthException(AuthErrorCode.UNAUTHENTICATED);
            if (isRefresh && isRefreshTokenBlacklisted(token)) {
                throw new AuthException(AuthErrorCode.INVALID_TOKEN);
            } else if (!isRefresh && isAccessTokenBlacklisted(token)) {
                throw new AuthException(AuthErrorCode.INVALID_TOKEN);
            }
            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public void resetPassword(String token, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            SignedJWT signedJWT = verifyToken(token);
            String username = signedJWT.getJWTClaimsSet().getSubject();
            User user = userDomainRepository.getByUsername(username);
            user.changePassword(passwordEncoder.encode(resetKeycloakPasswordCmd.getValue()));

            var keycloakToken = keycloakIdentityClient.getToken(GetTokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());
            keycloakIdentityClient.resetPassword("Bearer " + keycloakToken.getAccessToken(), user.getProviderId().toString(), resetKeycloakPasswordCmd);
            WriteLogCmd cmd = commandMapper.from("Change password");
            UserActivityLog userActivityLog = new UserActivityLog(cmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);
            emailService.sendMailAlert(user.getEmail(), "change_password");

        } catch (Exception ex) {
            throw new AuthException(AuthErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void addAccessTokenToBlacklist(String token, long expiryTimeMillis) {
        long expiryTime = System.currentTimeMillis() + expiryTimeMillis;
        redisTemplate.opsForZSet().add(INVALID_TOKEN_CACHE, token, expiryTime);
    }

    private void addRefreshTokenToBlacklist(String token, long expiryTimeMillis) {
        long expiryTime = System.currentTimeMillis() + expiryTimeMillis;
        redisTemplate.opsForZSet().add(INVALID_REFRESH_TOKEN_CACHE, token, expiryTime);
    }

    private boolean isAccessTokenBlacklisted(String token) {
        Double score = redisTemplate.opsForZSet().score(INVALID_TOKEN_CACHE, token);
        return score != null && score > System.currentTimeMillis();
    }

    private boolean isRefreshTokenBlacklisted(String token) {
        Double score = redisTemplate.opsForZSet().score(INVALID_REFRESH_TOKEN_CACHE, token);
        return score != null && score > System.currentTimeMillis();
    }
}
