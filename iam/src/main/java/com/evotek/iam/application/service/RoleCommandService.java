package com.evotek.iam.application.service;

import com.evotek.iam.application.dto.request.CreateOrUpdateRoleRequest;
import com.evotek.iam.application.dto.response.RoleDTO;

public interface RoleCommandService {
    RoleDTO createRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest);

    RoleDTO updateRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest);
}
