package com.evotek.iam.application.dto.mapper;

import org.mapstruct.Mapper;

import com.evotek.iam.application.dto.response.RoleDTO;
import com.evotek.iam.domain.Role;

@Mapper(componentModel = "spring")
public interface RoleDTOMapper extends DTOMapper<RoleDTO, Role> {}
