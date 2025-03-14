package com.evotek.iam.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

import com.evotek.iam.domain.UserRole;
import com.evotek.iam.infrastructure.persistence.entity.UserRoleEntity;

@Mapper(componentModel = "Spring")
public interface UserRoleEntityMapper extends EntityMapper<UserRole, UserRoleEntity> {}
