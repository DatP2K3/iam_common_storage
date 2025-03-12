package com.evotek.iam.application.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
public class CredentialRequest {
    private String type;
    private String value;
    private boolean temporary;
}
