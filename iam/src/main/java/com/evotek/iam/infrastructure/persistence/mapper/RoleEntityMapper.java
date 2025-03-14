package com.evotek.iam.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

import com.evotek.iam.domain.Role;
import com.evotek.iam.infrastructure.persistence.entity.RoleEntity;

@Mapper(componentModel = "Spring")
public interface RoleEntityMapper extends EntityMapper<Role, RoleEntity> {}
