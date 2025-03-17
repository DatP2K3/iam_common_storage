package com.evotek.iam.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evo.common.mapper.EntityMapper;
import com.evotek.iam.domain.RolePermission;
import com.evotek.iam.infrastructure.persistence.entity.RolePermissionEntity;

@Mapper(componentModel = "Spring")
public interface RolePermissionEntityMapper extends EntityMapper<RolePermission, RolePermissionEntity> {}
