package com.evotek.iam.infrastructure.adapter.notification.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.evotek.iam.infrastructure.adapter.notification.NotificationClient;
import com.evotek.iam.infrastructure.adapter.notification.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationClient notificationClient;

    @Override
    public void initUserTopic(UUID userId) {
        notificationClient.initUserTopic(userId);
    }
}
