package com.evotek.notification.infrastructure.persistence.repository;

import com.evotek.notification.infrastructure.persistence.entity.PushNotificationEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PushNotificationEventEntityRepository extends JpaRepository<PushNotificationEventEntity, UUID> {
}
