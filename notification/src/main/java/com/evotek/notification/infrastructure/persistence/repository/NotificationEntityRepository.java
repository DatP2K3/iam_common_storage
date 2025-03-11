package com.evotek.notification.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.notification.infrastructure.persistence.entity.NotificationEntity;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, UUID> {}
