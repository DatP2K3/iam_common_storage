package com.evotek.iam.infrastructure.adapter.keycloak.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.evotek.iam.application.dto.request.*;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakIdentityClient;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakService;
import com.evotek.iam.infrastructure.support.exception.ErrorNormalizer;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final KeycloakIdentityClient keycloakIdentityClient;
    private final ErrorNormalizer errorNormalizer;

    @Value("${idp.client-id}")
    private String clientId;

    @Value("${idp.client-secret}")
    private String clientSecret;

    @Override
    public String createKeycloakUser(CreateUserRequest request) {
        try {
            String token = getClientToken();
            var creationResponse = keycloakIdentityClient.createUser(
                    "Bearer " + token,
                    CreateUserKeycloakRequest.builder()
                            .username(request.getUsername())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .email(request.getEmail())
                            .enabled(true)
                            .emailVerified(false)
                            .credentials(List.of(CredentialRequest.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(request.getPassword())
                                    .build()))
                            .build());

            return extractUserId(creationResponse);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public void resetPassword(UUID keycloakUserId, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        String token = getClientToken();
        try {
            keycloakIdentityClient.resetPassword(
                    "Bearer " + token, keycloakUserId.toString(), resetKeycloakPasswordCmd);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = Optional.ofNullable(response.getHeaders().get("Location"))
                .map(list -> list.isEmpty() ? null : list.getFirst())
                .orElse("");
        String[] splitStr = location.split("/");
        return splitStr[splitStr.length - 1];
    }

    @Override
    public void logout(String authorizationHeader, String refreshToken) {
        try {
            keycloakIdentityClient.logout(
                    authorizationHeader,
                    LogoutRequest.builder()
                            .client_id(clientId)
                            .client_secret(clientSecret)
                            .refresh_token(refreshToken)
                            .build());
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public TokenDTO refreshToken(String refreshToken) {
        try {
            RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .grant_type("refresh_token")
                    .refresh_token(refreshToken)
                    .build();
            return keycloakIdentityClient.refreshToken(refreshTokenRequest);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public String getClientToken() {
        try {
            TokenDTO tokenDTO = keycloakIdentityClient.getToken(GetTokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());
            return tokenDTO.getAccessToken();
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public TokenDTO authenticate(LoginRequest request) {
        try {
            return keycloakIdentityClient.getToken(GetTokenRequest.builder()
                    .grant_type("password")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .build());
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public void lockUser(UUID userId, boolean enabled) {
        String token = getClientToken();
        LockUserRequest lockUserRequest =
                LockUserRequest.builder().enabled(enabled).build();
        try {
            keycloakIdentityClient.lockUser("Bearer " + token, userId.toString(), lockUserRequest);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }
}
