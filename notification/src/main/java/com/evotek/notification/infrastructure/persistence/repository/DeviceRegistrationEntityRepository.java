package com.evotek.notification.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evotek.notification.infrastructure.persistence.entity.DeviceRegistrationEntity;

public interface DeviceRegistrationEntityRepository extends JpaRepository<DeviceRegistrationEntity, UUID> {

    @Query("SELECT d FROM DeviceRegistrationEntity d WHERE :topic LIKE CONCAT('%', d.topics, '%') AND d.enabled = true")
    List<DeviceRegistrationEntity> findByTopic(@Param("topic") String topic);

    Optional<DeviceRegistrationEntity> findByDeviceIdAndUserId(UUID deviceId, UUID userId);

    List<DeviceRegistrationEntity> findByDeviceTokenAndEnabledTrue(String deviceToken);

    List<DeviceRegistrationEntity> findByUserIdAndEnabledTrue(UUID userId);
}
