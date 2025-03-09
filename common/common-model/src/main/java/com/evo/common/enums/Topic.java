package com.evo.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topic {
    SEND_NOTIFICATION_GROUP("send-notification-group"),
    PUSH_NOTIFICATION_GROUP("push-notification-group");
    private final String topicName;
}
