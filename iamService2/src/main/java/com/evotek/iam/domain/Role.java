package com.evotek.iam.domain;

import com.evotek.iam.domain.command.CreateOrUpdateRoleCmd;
import com.evotek.iam.domain.command.CreateRolePermissionCmd;
import com.evotek.iam.infrastructure.support.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Role extends Auditor {
    private UUID id;
    private String name;
    private String description;
    private boolean root;
    private List<RolePermission> rolePermissions;

    public Role(CreateOrUpdateRoleCmd cmd) {
        if(cmd.getId() != null) {
            this.id = cmd.getId();
        } else {
            this.id = IdUtils.nextId();
        }
        this.name = cmd.getName();
        this.description = cmd.getDescription();
        this.root = cmd.isRoot();

        this.rolePermissions = new ArrayList<>();
        if(cmd.getRolePermission() != null) {
            cmd.getRolePermission().forEach(createRolePermissionCmd -> {
                createRolePermissionCmd.setRoleId(this.id);
                rolePermissions.add(new RolePermission(createRolePermissionCmd));
            });
        }
    }

    public Role update(CreateOrUpdateRoleCmd cmd) {
        this.name = cmd.getName();
        this.description = cmd.getDescription();
        this.root = cmd.isRoot();

        if (this.rolePermissions == null) {
            this.rolePermissions = new ArrayList<>();
        }

        // Tạo map chứa rolePermission hiện tại
        Map<UUID, RolePermission> existingPermissionsMap = this.rolePermissions.stream()
                .collect(Collectors.toMap(RolePermission::getPermissionId, Function.identity()));

        // Chuyển danh sách mới thành Set UUID
        Set<UUID> newPermissionIds = cmd.getRolePermission().stream()
                .map(CreateRolePermissionCmd::getPermissionId)
                .collect(Collectors.toSet());

        // Danh sách mới sau khi cập nhật
        List<RolePermission> updatedRolePermissions = new ArrayList<>();

        // Thêm hoặc cập nhật permission
        for (CreateRolePermissionCmd createRolePermissionCmd : cmd.getRolePermission()) {
            createRolePermissionCmd.setRoleId(this.id);
            RolePermission existing = existingPermissionsMap.get(createRolePermissionCmd.getPermissionId());

            if (existing != null) {
                // Nếu có sẵn và đang bị xóa thì bỏ cờ deleted
                if (existing.isDeleted()) {
                    existing.setDeleted(false);
                }
                updatedRolePermissions.add(existing);
            } else {
                // Nếu chưa có thì tạo mới
                updatedRolePermissions.add(new RolePermission(createRolePermissionCmd));
            }
        }

        // Xóa mềm những permission không có trong danh sách mới
        for (RolePermission rp : this.rolePermissions) {
            if (!newPermissionIds.contains(rp.getPermissionId())) {
                rp.setDeleted(true);
                updatedRolePermissions.add(rp);
            }
        }

        this.rolePermissions = updatedRolePermissions;
        return this;
    }
}
