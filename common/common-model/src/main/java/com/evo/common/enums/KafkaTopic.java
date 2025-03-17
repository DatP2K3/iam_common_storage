package com.evo.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {
    SEND_NOTIFICATION_GROUP("send-notification-group"),
    PUSH_NOTIFICATION_GROUP("push-notification-group"),
    SYNC_USER_GROUP("sync-user-group");
    private final String topicName;
}
