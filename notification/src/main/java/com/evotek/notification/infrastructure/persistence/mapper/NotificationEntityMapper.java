package com.evotek.notification.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evo.common.mapper.EntityMapper;
import com.evotek.notification.domain.Notification;
import com.evotek.notification.infrastructure.persistence.entity.NotificationEntity;

@Mapper(componentModel = "Spring")
public interface NotificationEntityMapper extends EntityMapper<Notification, NotificationEntity> {}
