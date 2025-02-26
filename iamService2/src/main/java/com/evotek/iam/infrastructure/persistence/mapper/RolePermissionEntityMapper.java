package com.evotek.iam.infrastructure.persistence.mapper;

import com.evotek.iam.domain.RolePermission;
import com.evotek.iam.infrastructure.persistence.entity.RolePermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface RolePermissionEntityMapper extends EntityMapper<RolePermission, RolePermissionEntity> {
}
