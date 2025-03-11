package com.evotek.iam.application.dto.request.identityKeycloak;

import lombok.*;

@Getter
@Setter
@Builder
public class CredentialRequest {
    private String type;
    private String value;
    private boolean temporary;
}
