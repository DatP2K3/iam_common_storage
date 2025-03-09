package com.evotek.notification.application.service.push.impl;

import com.evotek.notification.domain.repository.DeviceRegistrationDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceRegistrationCommandService {
    private final DeviceRegistrationDomainRepository deviceRegistrationDomainRepository;


}
