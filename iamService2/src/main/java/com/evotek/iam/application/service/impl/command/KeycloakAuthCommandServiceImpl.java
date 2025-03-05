package com.evotek.iam.application.service.impl.command;

import com.evotek.iam.application.dto.request.LoginRequest;
import com.evotek.iam.application.dto.request.identityKeycloak.LogoutRequest;
import com.evotek.iam.application.dto.request.identityKeycloak.RefreshTokenRequest;
import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;
import com.evotek.iam.application.dto.request.identityKeycloak.GetTokenRequest;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.domain.User;
//import com.evotek.iam.domain.UserActivityLog;
//import com.evotek.iam.domain.command.WriteLogCmd;
//import com.evotek.iam.domain.repository.UserActivityLogDomainRepository;
import com.evotek.iam.domain.command.WriteLogCmd;
import com.evotek.iam.domain.repository.UserDomainRepository;
import com.evotek.iam.infrastructure.adapter.email.EmailService;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;
import com.evotek.iam.application.service.AuthServiceCommand;
import com.evotek.iam.infrastructure.support.exception.ErrorNormalizer;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakIdentityClient;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component("keycloak_auth_command_service")
@RequiredArgsConstructor
public class KeycloakAuthCommandServiceImpl implements AuthServiceCommand {
    private final KeycloakIdentityClient keycloakIdentityClient;
    private final ErrorNormalizer errorNormalizer;
    private  final PasswordEncoder passwordEncoder;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final EmailService emailService;
    private final UserDomainRepository userDomainRepository;
    private final CommandMapper commandMapper;

    @Value("${idp.client-id}")
    private String clientId;
    @Value("${idp.client-secret}")
    private String clientSecret;

    @Override
    public TokenDTO authenticate(LoginRequest loginRequest) {
        try {
            TokenDTO tokenDTO = keycloakIdentityClient.getToken(GetTokenRequest.builder()
                                .grant_type("password")
                                .client_id(clientId)
                                .client_secret(clientSecret)
                                .scope("openid")
                                .username(loginRequest.getUsername())
                                .password(loginRequest.getPassword())
                                .build());

            User user = userDomainRepository.getByUsername(loginRequest.getUsername());
            WriteLogCmd logCmd = commandMapper.from("Login");
            UserActivityLog userActivityLog = new UserActivityLog(logCmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);
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
            String access_jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            redisTemplate.opsForValue().set(access_jit, expiryTime, 3600, TimeUnit.MILLISECONDS);
            keycloakIdentityClient.logout(authorizationHeader, LogoutRequest.builder()
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .refresh_token(refreshToken)
                    .build());
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
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("refresh_token")
                .refresh_token(refreshToken)
                .build();
        return keycloakIdentityClient.refreshToken(refreshTokenRequest);
    }
    @Override
    public void requestPasswordReset(String username, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            User user = userDomainRepository.getByUsername(username);
            String userId = user.getProviderId().toString();
            var token = keycloakIdentityClient.getToken(GetTokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            keycloakIdentityClient.resetPassword("Bearer " + token.getAccessToken(), userId, resetKeycloakPasswordCmd);
            user.changePassword(passwordEncoder.encode(resetKeycloakPasswordCmd.getValue()));

            WriteLogCmd logCmd = commandMapper.from("Change password");
            UserActivityLog userActivityLog = new UserActivityLog(logCmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);
            emailService.sendMailAlert(user.getEmail(), "change_password");
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public void resetPassword(String token, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
    }
}