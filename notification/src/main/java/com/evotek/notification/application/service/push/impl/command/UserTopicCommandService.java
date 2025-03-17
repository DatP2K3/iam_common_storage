package com.evotek.notification.application.service.push.impl.command;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evo.common.dto.request.UpdateTopicsOfUserRequest;
import com.evo.common.enums.FCMTopic;
import com.evotek.notification.domain.UserTopic;
import com.evotek.notification.domain.command.CreateUserTopicCmd;
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
public class UserTopicCommandService {
    private final UserTopicDomainRepository userTopicDomainRepository;
    private final DeviceRegistrationDomainRepository deviceRegistrationDomainRepository;

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

    public void updateTopicOfUser(UpdateTopicsOfUserRequest updateTopicsOfUserRequest) {
        List<UserTopic> existingUserTopics =
                userTopicDomainRepository.findByUserId(updateTopicsOfUserRequest.getUserId());
        Map<String, UserTopic> existingUserTopicMap = existingUserTopics.stream()
                .map(ut -> {
                    ut.setEnabled(false);
                    return ut;
                })
                .collect(Collectors.toMap(UserTopic::getTopic, ut -> ut));

        updateTopicsOfUserRequest.getTopics().forEach(topic -> {
            if (existingUserTopicMap.containsKey(topic)) {
                existingUserTopicMap.get(topic).setEnabled(true);
            } else {
                CreateUserTopicCmd createUserTopicCmd = CreateUserTopicCmd.builder()
                        .userId(updateTopicsOfUserRequest.getUserId())
                        .topic(topic)
                        .enabled(true)
                        .build();
                UserTopic userTopic = new UserTopic(createUserTopicCmd);
                existingUserTopics.add(userTopic);
            }
        });
        userTopicDomainRepository.saveAll(existingUserTopics);

        List<String> deviceTokens =
                deviceRegistrationDomainRepository.getDeviceTokensByUserId(updateTopicsOfUserRequest.getUserId());

        existingUserTopics.forEach(userTopic -> {
            try {
                if (userTopic.isEnabled()) {
                    FirebaseMessaging.getInstance(FirebaseApp.getInstance("my-app"))
                            .subscribeToTopic(deviceTokens, userTopic.getTopic());
                } else {
                    FirebaseMessaging.getInstance(FirebaseApp.getInstance("my-app"))
                            .unsubscribeFromTopic(deviceTokens, userTopic.getTopic());
                }
            } catch (FirebaseMessagingException e) {
                throw new AppException(AppErrorCode.FIREBASE_SUBSCRIBE_TOPIC_FAILED);
            }
        });
    }
}
