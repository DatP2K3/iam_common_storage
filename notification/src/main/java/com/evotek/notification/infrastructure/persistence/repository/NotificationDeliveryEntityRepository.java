package com.evotek.notification.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.notification.infrastructure.persistence.entity.NotificationDeliveryEntity;

public interface NotificationDeliveryEntityRepository extends JpaRepository<NotificationDeliveryEntity, UUID> {
    List<NotificationDeliveryEntity> findByDeviceRegistrationIdIn(List<UUID> deviceIds);
}
