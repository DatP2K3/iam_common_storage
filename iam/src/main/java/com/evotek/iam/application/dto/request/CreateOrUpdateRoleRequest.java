package com.evotek.iam.application.dto.request;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateRoleRequest {
    private UUID id;
    private String name;
    private String description;
    private boolean root;
    private List<CreateRolePermissionRequest> rolePermission;
}
