package com.evotek.iam.infrastructure.adapter.keycloak;

import com.evotek.iam.application.dto.request.LoginRequest;
import com.evotek.iam.application.dto.response.TokenDTO;

public interface KeycloakQueryClient {
    String getClientToken();

    TokenDTO authenticate(LoginRequest request);
}
