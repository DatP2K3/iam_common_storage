package com.evotek.iam.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evo.common.mapper.EntityMapper;
import com.evotek.iam.domain.Role;
import com.evotek.iam.infrastructure.persistence.entity.RoleEntity;

@Mapper(componentModel = "Spring")
public interface RoleEntityMapper extends EntityMapper<Role, RoleEntity> {}
