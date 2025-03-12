package com.evotek.notification.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.domain.repository.DeviceRegistrationDomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduledTask {
    private final DeviceRegistrationDomainRepository deviceRegistrationDomainRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void runAtMidnight() {
        Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);
        List<DeviceRegistration> deviceRegistrations =
                deviceRegistrationDomainRepository.findInactivatedDevices(thirtyDaysAgo);
        deviceRegistrationDomainRepository.hardDeleteDeviceRegistration(deviceRegistrations);
    }
}
