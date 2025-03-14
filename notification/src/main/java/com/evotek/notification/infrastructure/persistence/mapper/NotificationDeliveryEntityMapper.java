package com.evotek.notification.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evo.common.mapper.EntityMapper;
import com.evotek.notification.domain.NotificationDelivery;
import com.evotek.notification.infrastructure.persistence.entity.NotificationDeliveryEntity;

@Mapper(componentModel = "Spring")
public interface NotificationDeliveryEntityMapper
        extends EntityMapper<NotificationDelivery, NotificationDeliveryEntity> {}
