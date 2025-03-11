package com.evotek.notification.application.service.push.impl.query;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.domain.repository.DeviceRegistrationDomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceRegistrationQueryService {
    private final DeviceRegistrationDomainRepository deviceRegistrationDomainRepository;

    public List<DeviceRegistration> getDeviceByTokenAndEnable(String token) {
        return deviceRegistrationDomainRepository.findByDeviceTokenAndEnabled(token);
    }

    public List<DeviceRegistration> getDeviceByTopicAndEnabled(String topic) {
        return deviceRegistrationDomainRepository.findByTopic(topic);
    }

    public List<DeviceRegistration> getDeviceByUserIdAndEnable(UUID token) {
        return deviceRegistrationDomainRepository.findByUserIdAndEnabled(token);
    }
}
