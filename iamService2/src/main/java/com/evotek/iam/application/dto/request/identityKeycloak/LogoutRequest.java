package com.evotek.iam.application.dto.request.identityKeycloak;

import lombok.*;

@Getter
@Setter
@Builder
public class LogoutRequest {
    private String client_id;
    private String client_secret;
    private String refresh_token;
}
