package com.evotek.iam.infrastructure.adapter.keycloak;

import java.util.UUID;

import com.evotek.iam.application.dto.request.CreateUserRequest;
import com.evotek.iam.application.dto.request.LoginRequest;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;

public interface KeycloakService {
    String createKeycloakUser(CreateUserRequest request);

    void resetPassword(UUID keycloakUserId, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);

    void logout(String authorizationHeader, String refreshToken);

    TokenDTO refreshToken(String refreshToken);

    String getClientToken();

    TokenDTO authenticate(LoginRequest request);

    void lockUser(UUID userId, boolean enabled);
}
