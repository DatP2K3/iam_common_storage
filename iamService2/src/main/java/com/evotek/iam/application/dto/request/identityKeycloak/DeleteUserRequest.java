package com.evotek.iam.application.dto.request.identityKeycloak;

import lombok.*;

@Getter
@Setter
@Builder
public class DeleteUserRequest {
    private String deletedBody;
}
