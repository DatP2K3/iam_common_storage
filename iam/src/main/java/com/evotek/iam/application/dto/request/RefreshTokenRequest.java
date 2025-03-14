package com.evotek.iam.application.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefreshTokenRequest {
    private String client_id;
    private String client_secret;
    private String grant_type;
    private String refresh_token;
}
