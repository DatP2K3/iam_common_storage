package com.evotek.iam.application.mapper;

import org.mapstruct.Mapper;

import com.evotek.iam.application.dto.request.*;
import com.evotek.iam.domain.command.*;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    CreateOrUpdatePermissionCmd from(CreateOrUpdatePermissionRequest request);

    LoginCmd from(LoginRequest request);

    VerifyOtpCmd from(VerifyOtpRequest request);

    CreateUserCmd from(CreateUserRequest request);

    ChangePasswordCmd from(ChangePasswordRequest request);

    CreateOrUpdateRoleCmd from(CreateOrUpdateRoleRequest request);

    DeleteRolePermissionCmd from(DeleteRolePermissionRequest request);

    CreateRolePermissionCmd from(CreateRolePermissionRequest request);

    CreateUserRoleCmd from(CreateUserRoleRequest request);

    UpdateUserCmd from(UpdateUserRequest request);

    WriteLogCmd from(String activity);
}
