package com.evotek.iam.application.dto.request.identityKeycloak;

import lombok.*;

@Getter
@Setter
@Builder
public class GetTokenRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
    private String username;
    private String password;
}
