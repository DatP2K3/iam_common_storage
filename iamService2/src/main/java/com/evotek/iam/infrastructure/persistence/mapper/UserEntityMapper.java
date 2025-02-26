package com.evotek.iam.infrastructure.persistence.mapper;

import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.User;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;
import com.evotek.iam.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface UserEntityMapper extends EntityMapper<User, UserEntity> {
}
