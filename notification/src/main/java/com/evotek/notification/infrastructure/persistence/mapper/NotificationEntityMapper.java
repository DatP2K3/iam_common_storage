package com.evotek.notification.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evotek.notification.domain.PushNotificationEvent;
import com.evotek.notification.infrastructure.persistence.entity.PushNotificationEventEntity;

@Mapper(componentModel = "Spring")
public interface PushNotificationEventEntityMapper
        extends EntityMapper<PushNotificationEventEntity, PushNotificationEvent> {}
