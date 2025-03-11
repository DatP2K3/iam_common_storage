package com.evotek.iam.application.service;

import com.evotek.iam.application.dto.request.CreateOrUpdatePermissionRequest;
import com.evotek.iam.application.dto.response.PermissionDTO;

public interface PermissionCommandService {
    PermissionDTO createPermission(CreateOrUpdatePermissionRequest request);

    PermissionDTO updatePermission(CreateOrUpdatePermissionRequest request);
}
