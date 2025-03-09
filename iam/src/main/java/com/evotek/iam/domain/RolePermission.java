package com.evotek.iam.domain;

import java.util.Objects;
import java.util.UUID;

import com.evo.common.Auditor;
import com.evotek.iam.domain.command.CreateRolePermissionCmd;
import com.evotek.iam.infrastructure.support.IdUtils;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class RolePermission extends Auditor {
    private UUID id;
    private UUID roleId;
    private UUID permissionId;
    private boolean deleted;

    public RolePermission(CreateRolePermissionCmd cmd) {
        this.id = IdUtils.nextId();
        this.roleId = cmd.getRoleId();
        this.permissionId = cmd.getPermissionId();
        this.deleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, permissionId);
    }
}
