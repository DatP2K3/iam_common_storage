package com.evotek.notification.application.service.push.impl.command;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.evotek.notification.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evotek.notification.application.dto.request.UnRegisterDeviceRequest;
import com.evotek.notification.application.mapper.CommandMapper;
import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.domain.command.RegisterOrUpdateDeviceCmd;
import com.evotek.notification.domain.repository.DeviceRegistrationDomainRepository;
import com.evotek.notification.domain.repository.UserTopicDomainRepository;
import com.evotek.notification.infrastructure.support.exception.AppErrorCode;
import com.evotek.notification.infrastructure.support.exception.AppException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceRegistrationCommandService {
    private final DeviceRegistrationDomainRepository deviceRegistrationDomainRepository;
    private final CommandMapper commandMapper;
    private final UserTopicDomainRepository userTopicDomainRepository;

    public void registerDevice(RegisterOrUpdateDeviceRequest request) {
        RegisterOrUpdateDeviceCmd registerOrUpdateDeviceCmd = commandMapper.from(request);

        DeviceRegistration existingDeviceRegistration =
                deviceRegistrationDomainRepository.findByDeviceIdAndUserId(request.getDeviceId(), request.getUserId());
        if (existingDeviceRegistration != null && !existingDeviceRegistration.isEnabled()) {
            existingDeviceRegistration.setEnabled(true);
            deviceRegistrationDomainRepository.save(existingDeviceRegistration);
        } else {
            DeviceRegistration deviceRegistration = new DeviceRegistration(registerOrUpdateDeviceCmd);
            subscribeTokenTopic(
                    deviceRegistration,
                    registerOrUpdateDeviceCmd.getDeviceToken(),
                    registerOrUpdateDeviceCmd.getUserId());
            deviceRegistrationDomainRepository.save(deviceRegistration);
        }
    }

    public void unregisterDevice(UnRegisterDeviceRequest unRegisterDeviceRequest) {
        DeviceRegistration deviceRegistration = deviceRegistrationDomainRepository.findByDeviceIdAndUserId(
                unRegisterDeviceRequest.getDeviceId(), unRegisterDeviceRequest.getUserId());
        if (deviceRegistration != null) {
            deviceRegistration.setEnabled(false);
            deviceRegistration.removeAllSubscribedTopics();
            deviceRegistrationDomainRepository.save(deviceRegistration);
            unsubscribeDeviceFromTopic(unRegisterDeviceRequest, deviceRegistration.getTopics());
        }
    }

    public void subscribeTokenTopic(DeviceRegistration deviceRegistration, String deviceToken, UUID userId) {
        List<String> fcmTopics = userTopicDomainRepository.findTopicEnabled(userId);
        fcmTopics.forEach(topic -> {
            try {
                FirebaseMessaging.getInstance(FirebaseApp.getInstance("my-app"))
                        .subscribeToTopic(List.of(deviceToken), topic);
                deviceRegistration.addSubscribedTopic(topic);
            } catch (FirebaseMessagingException e) {
                throw new AppException(AppErrorCode.FIREBASE_SUBSCRIBE_TOPIC_FAILED);
            }
        });
    }

    public void unsubscribeDeviceFromTopic(UnRegisterDeviceRequest unRegisterDeviceRequest, List<String> topics) {
        topics.forEach(topic -> {
            try {
                FirebaseMessaging.getInstance(FirebaseApp.getInstance("my-app"))
                        .unsubscribeFromTopic(List.of(unRegisterDeviceRequest.getDeviceToken()), topic);
            } catch (FirebaseMessagingException e) {
                throw new AppException(AppErrorCode.FIREBASE_SUBSCRIBE_TOPIC_FAILED);
            }
            ;
        });
    }
}
