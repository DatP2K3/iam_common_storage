package com.evotek.iam.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evo.common.mapper.EntityMapper;
import com.evotek.iam.domain.Permission;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;

@Mapper(componentModel = "Spring")
public interface PermissionEntityMapper extends EntityMapper<Permission, PermissionEntity> {}
