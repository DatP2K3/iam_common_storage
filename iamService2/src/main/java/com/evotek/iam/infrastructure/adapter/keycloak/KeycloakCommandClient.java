package com.evotek.iam.infrastructure.adapter.keycloak;

import com.evotek.iam.application.dto.request.CreateUserRequest;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;

import java.util.UUID;

public interface KeycloakCommandClient {
    String createKeycloakUser(CreateUserRequest request);
    void resetPassword(String token, UUID keycloakUserId, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);
}
