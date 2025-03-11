package com.evotek.iam.infrastructure.adapter.keycloak;

import java.util.UUID;

import com.evotek.iam.application.dto.request.CreateUserRequest;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;

public interface KeycloakCommandClient {
    String createKeycloakUser(CreateUserRequest request);

    void resetPassword(String token, UUID keycloakUserId, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);

    void logout(String authorizationHeader, String refreshToken);

    TokenDTO refreshToken(String refreshToken);
}
