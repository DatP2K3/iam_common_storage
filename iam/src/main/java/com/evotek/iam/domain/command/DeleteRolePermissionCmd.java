package com.evotek.iam.domain.command;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRolePermissionCmd {
    private UUID roleId;
    private UUID permissionId;
}
