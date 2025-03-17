package com.evotek.iam.application.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetTokenRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
    private String username;
    private String password;
}
