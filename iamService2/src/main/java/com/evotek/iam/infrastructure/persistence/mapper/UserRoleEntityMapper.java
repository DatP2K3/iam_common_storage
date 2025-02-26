package com.evotek.iam.infrastructure.persistence.mapper;

import com.evotek.iam.domain.UserRole;
import com.evotek.iam.infrastructure.persistence.entity.UserRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface UserRoleEntityMapper extends EntityMapper<UserRole, UserRoleEntity> {
}
