package com.evotek.iam.service.keycloak;

import com.evotek.iam.configuration.TokenProvider;
import com.evotek.iam.dto.request.LoginRequest;
import com.evotek.iam.dto.request.identityKeycloak.LogoutRequest;
import com.evotek.iam.dto.request.identityKeycloak.RefreshTokenRequest;
import com.evotek.iam.dto.request.identityKeycloak.ResetPasswordRequest;
import com.evotek.iam.dto.request.identityKeycloak.TokenRequest;
import com.evotek.iam.dto.response.TokenResponse;
import com.evotek.iam.exception.AppException;
import com.evotek.iam.exception.ErrorCode;
import com.evotek.iam.exception.ErrorNormalizer;
import com.evotek.iam.model.*;
import com.evotek.iam.repository.*;
import com.evotek.iam.service.common.AuthService;
import com.evotek.iam.service.common.EmailService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component("keycloak_auth_service")
@RequiredArgsConstructor
public class KeycloakAuthService implements AuthService {
    private final IdentityClient identityClient;
    private final ErrorNormalizer errorNormalizer;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OauthClientRepository oauthClientRepository;
    private final TokenProvider tokenProvider;

    @Value("${idp.client-id}")
    private String clientId;
    @Value("${idp.client-secret}")
    private String clientSecret;
    @Override
    public boolean introspect(String token) {
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public TokenResponse authenticate(LoginRequest loginRequest) {
        try {
            return identityClient.getToken(TokenRequest.builder()
                    .grant_type("password")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .username(loginRequest.getUsername())
                    .password(loginRequest.getPassword())
                    .build());
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
            identityClient.logout(authorizationHeader, LogoutRequest.builder()
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .refresh_token(refreshToken)
                    .build());
        } catch (FeignException exception) {
            throw errorNormalizer.handleKeyCloakException(exception);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public TokenResponse refresh(String refreshToken) {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("refresh_token")
                .refresh_token(refreshToken)
                .build();
        return identityClient.refreshToken(refreshTokenRequest);
    }

    @Override
    public void requestPasswordReset(String username, ResetPasswordRequest resetPasswordRequest) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            String userId = user.getProviderId();

            var token = identityClient.getToken(TokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            identityClient.resetPassword("Bearer " + token.getAccessToken(), userId, resetPasswordRequest);

            user.setPassword( resetPasswordRequest.getValue());
            userRepository.save(user);

            emailService.sendMailAlert(user.getEmail(), "change_password");
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {
    }

    @Override
    public String getClientToken(String clientId, String clientSecret) {
        OauthClient oauthClient = oauthClientRepository.findByClientId(clientId).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXISTED));
        if (!oauthClient.getClientSecret().equals(clientSecret)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(clientId)
                .issuer("evotek.iam.com")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", "common")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        RSASSASigner signer = new RSASSASigner(tokenProvider.getKeyPair().getPrivate());

        try {
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private boolean verifyToken(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String access_jit = signedJWT.getJWTClaimsSet().getJWTID();
            if (redisTemplate.hasKey(access_jit)) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return true;
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}