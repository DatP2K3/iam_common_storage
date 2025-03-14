package com.evotek.iam.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

import com.evotek.iam.domain.Permission;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;

@Mapper(componentModel = "Spring")
public interface PermissionEntityMapper extends EntityMapper<Permission, PermissionEntity> {}
