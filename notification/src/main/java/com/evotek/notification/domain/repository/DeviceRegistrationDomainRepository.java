package com.evotek.notification.domain.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.infrastructure.domainrepository.DomainRepository;

public interface DeviceRegistrationDomainRepository extends DomainRepository<DeviceRegistration, UUID> {
    List<DeviceRegistration> findByUserIdAndEnabled(UUID userId);

    List<DeviceRegistration> findByDeviceTokenAndEnabled(String deviceToken);

    DeviceRegistration findByDeviceIdAndUserId(UUID deviceId, UUID userId);

    List<String> getDeviceTokensByUserId(UUID userId);

    List<DeviceRegistration> findByUserIdInAndEnabledTrue(List<UUID> userIds);

    List<DeviceRegistration> findInactivatedDevices(Instant cutoffDate);

    void hardDeleteDeviceRegistration(List<DeviceRegistration> deviceRegistrations);
}
