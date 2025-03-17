package com.evotek.iam.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evo.common.mapper.EntityMapper;
import com.evotek.iam.domain.User;
import com.evotek.iam.infrastructure.persistence.entity.UserEntity;

@Mapper(componentModel = "Spring")
public interface UserEntityMapper extends EntityMapper<User, UserEntity> {}
