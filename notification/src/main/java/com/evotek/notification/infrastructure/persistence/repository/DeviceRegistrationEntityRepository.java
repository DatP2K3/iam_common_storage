package com.evotek.notification.infrastructure.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evotek.notification.infrastructure.persistence.entity.DeviceRegistrationEntity;

public interface DeviceRegistrationEntityRepository extends JpaRepository<DeviceRegistrationEntity, UUID> {

    Optional<DeviceRegistrationEntity> findByDeviceIdAndUserId(UUID deviceId, UUID userId);

    List<DeviceRegistrationEntity> findByDeviceTokenAndEnabledTrue(String deviceToken);

    List<DeviceRegistrationEntity> findByUserIdAndEnabledTrue(UUID userId);

    @Query("SELECT d.deviceToken FROM DeviceRegistrationEntity d WHERE d.userId = :userId AND d.enabled = true")
    List<String> findDeviceTokenByUserId(@Param("userId") UUID userId);

    List<DeviceRegistrationEntity> findByUserIdInAndEnabledTrue(List<UUID> userId);

    @Query("SELECT d FROM DeviceRegistrationEntity d WHERE d.lastLoginAt < :cutOffDate AND d.enabled = false")
    List<DeviceRegistrationEntity> findInactivatedDevices(@Param("cutOffDate") Instant cutOffDate);
}
