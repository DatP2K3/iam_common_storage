package com.evotek.iam.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.infrastructure.persistence.entity.UserActivityLogEntity;

@Mapper(componentModel = "Spring")
public interface UserActivityLogEntityMapper extends EntityMapper<UserActivityLog, UserActivityLogEntity> {}
