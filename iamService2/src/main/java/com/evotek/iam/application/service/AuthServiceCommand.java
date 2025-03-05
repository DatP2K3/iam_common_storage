package com.evotek.iam.application.service;

import com.evotek.iam.application.dto.request.LoginRequest;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;
import com.evotek.iam.application.dto.response.TokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthServiceCommand {
    TokenDTO authenticate(LoginRequest loginRequest);
    void logoutIam(HttpServletRequest request, String refreshToken);
    TokenDTO refresh(String refreshToken);
    void requestPasswordReset(String username, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);
    void resetPassword(String token, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);
}
