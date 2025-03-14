package com.evotek.notification.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evo.common.mapper.EntityMapper;
import com.evotek.notification.domain.UserTopic;
import com.evotek.notification.infrastructure.persistence.entity.UserTopicEntity;

@Mapper(componentModel = "Spring")
public interface UserTopicEntityMapper extends EntityMapper<UserTopic, UserTopicEntity> {}
