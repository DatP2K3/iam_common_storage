package com.evotek.iam.domain.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateOrUpdatePermissionCmd {
    private UUID id;
    private String resourceId;
    private String scope;
}
