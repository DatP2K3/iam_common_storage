package com.evotek.notification.domain.repository;

import java.util.List;
import java.util.UUID;

import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.infrastructure.domainrepository.DomainRepository;

public interface DeviceRegistrationDomainRepository extends DomainRepository<DeviceRegistration, UUID> {
    List<DeviceRegistration> findByUserIdAndEnabled(UUID userId);

    List<DeviceRegistration> findByTopic(String topic);

    List<DeviceRegistration> findByDeviceTokenAndEnabled(String deviceToken);

    DeviceRegistration findByDeviceIdAndUserId(UUID deviceId, UUID userId);
}
