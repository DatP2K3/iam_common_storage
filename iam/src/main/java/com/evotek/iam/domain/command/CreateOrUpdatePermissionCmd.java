package com.evotek.iam.domain.command;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdatePermissionCmd {
    private UUID id;
    private String resourceId;
    private String scope;
}
