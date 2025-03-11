package com.evotek.iam.application.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRolePermissionRequest {
    private UUID roleId;
    private UUID permissionId;
}
