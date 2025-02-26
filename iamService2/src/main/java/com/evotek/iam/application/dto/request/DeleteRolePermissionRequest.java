package com.evotek.iam.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRolePermissionRequest {
    private UUID roleId;
    private UUID permissionId;
}
