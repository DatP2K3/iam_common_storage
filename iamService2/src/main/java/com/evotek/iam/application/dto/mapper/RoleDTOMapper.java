package com.evotek.iam.application.dto.mapper;

import com.evotek.iam.application.dto.response.RoleDTO;
import com.evotek.iam.domain.Role;
import com.evotek.iam.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDTOMapper extends DTOMapper<RoleDTO, Role, RoleEntity> {

}
