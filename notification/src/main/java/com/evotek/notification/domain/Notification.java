package com.evotek.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushNotificationEvent {
    private UUID id;
    private String title;
    private String body;
    private String topic;
    private String token;
    private Map<String, Object> data;
}
