package com.evotek.notification.application.service.push.impl.command;

import com.evo.common.enums.FCMTopic;
import com.evotek.notification.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.domain.UserTopic;
import com.evotek.notification.domain.command.CreateUserTopicCmd;
import com.evotek.notification.domain.repository.DeviceRegistrationDomainRepository;
import com.evotek.notification.domain.repository.UserTopicDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTopicCommandService {
    private final UserTopicDomainRepository userTopicDomainRepository;
    private final DeviceRegistrationDomainRepository deviceRegistrationDomainRepository;
    private final DeviceRegistrationCommandService deviceRegistrationCommandService;

    public void initUserTopic(UUID userId) {
        List<String> topics = FCMTopic.getAllTopics();
        List<UserTopic> userTopics = topics.stream()
                .map(topic -> {
                    CreateUserTopicCmd createUserTopicCmd = CreateUserTopicCmd.builder()
                            .userId(userId)
                            .topic(topic)
                            .enabled(true)
                            .build();
                    return new UserTopic(createUserTopicCmd);
                })
                .toList();
            userTopicDomainRepository.saveAll(userTopics);
    }

    public void updateTopicOfUser(RegisterOrUpdateDeviceRequest registerOrUpdateDeviceRequest) {
        List<UserTopic> existingUserTopics =
                userTopicDomainRepository.findByUserId(registerOrUpdateDeviceRequest.getUserId());
        Map<String, UserTopic> existingUserTopicMap = existingUserTopics.stream()
                .peek(ut -> ut.setEnabled(false))
                .collect(Collectors.toMap(UserTopic::getTopic, ut -> ut));

        registerOrUpdateDeviceRequest.getTopics().forEach(topic -> {
            if (existingUserTopicMap.containsKey(topic)) {
                existingUserTopicMap.get(topic).setEnabled(true);
            } else {
                CreateUserTopicCmd createUserTopicCmd = CreateUserTopicCmd.builder()
                        .userId(registerOrUpdateDeviceRequest.getUserId())
                        .topic(topic)
                        .enabled(true)
                        .build();
                UserTopic userTopic = new UserTopic(createUserTopicCmd);
                existingUserTopics.add(userTopic);
            }
        });
        userTopicDomainRepository.saveAll(existingUserTopics);

        List<DeviceRegistration> deviceRegistrations =
                deviceRegistrationDomainRepository.findByUserIdAndEnabled(registerOrUpdateDeviceRequest.getUserId());
        deviceRegistrations.forEach(deviceRegistration -> {
            deviceRegistration.removeAllSubscribedTopics();
            deviceRegistrationCommandService.subscribeTokenTopic(
                    deviceRegistration, deviceRegistration.getDeviceToken(), deviceRegistration.getUserId());
            deviceRegistrationDomainRepository.save(deviceRegistration);
        });
    }
}
