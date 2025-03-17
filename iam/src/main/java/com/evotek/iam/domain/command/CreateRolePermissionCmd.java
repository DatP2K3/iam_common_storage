package com.evotek.iam.domain.command;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateRolePermissionCmd {
    private UUID roleId;
    private UUID permissionId;
}
