package com.evotek.iam.application.dto.request.identityKeycloak;

import lombok.*;

@Getter
@Setter
@Builder
public class RefreshTokenRequest {
    private String client_id;
    private String client_secret;
    private String grant_type;
    private String refresh_token;
}
