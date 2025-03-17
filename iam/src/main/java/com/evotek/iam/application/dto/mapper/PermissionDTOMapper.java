package com.evotek.iam.application.dto.mapper;

import org.mapstruct.Mapper;

import com.evotek.iam.application.dto.response.PermissionDTO;
import com.evotek.iam.domain.Permission;

@Mapper(componentModel = "spring")
public interface PermissionDTOMapper extends DTOMapper<PermissionDTO, Permission> {}
