package com.evotek.iam.infrastructure.adapter.notification;

import java.util.UUID;

public interface NotificationService {
    void initUserTopic(UUID userId);
}
