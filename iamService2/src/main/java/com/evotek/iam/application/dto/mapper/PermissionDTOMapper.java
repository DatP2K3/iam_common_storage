package com.evotek.iam.application.dto.mapper;

import com.evotek.iam.application.dto.response.PermissionDTO;
import com.evotek.iam.domain.Permission;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionDTOMapper extends DTOMapper<PermissionDTO, Permission, PermissionEntity> {

}
