package com.evotek.iam.infrastructure.persistence.mapper;

import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.infrastructure.persistence.entity.UserActivityLogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface UserLogActivityEntityMapper extends EntityMapper<UserActivityLog, UserActivityLogEntity> {
}
