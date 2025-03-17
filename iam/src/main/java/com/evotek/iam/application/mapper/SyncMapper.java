package com.evotek.iam.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.evo.common.dto.request.SyncUserRequest;
import com.evotek.iam.domain.User;

@Mapper(componentModel = "spring")
public interface SyncMapper {
    @Mapping(source = "userRole.roleId", target = "roleId")
    SyncUserRequest from(User user);
}
