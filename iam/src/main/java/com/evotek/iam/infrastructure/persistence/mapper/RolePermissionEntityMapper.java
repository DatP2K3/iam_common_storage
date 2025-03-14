package com.evotek.iam.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

import com.evotek.iam.domain.RolePermission;
import com.evotek.iam.infrastructure.persistence.entity.RolePermissionEntity;

@Mapper(componentModel = "Spring")
public interface RolePermissionEntityMapper extends EntityMapper<RolePermission, RolePermissionEntity> {}
