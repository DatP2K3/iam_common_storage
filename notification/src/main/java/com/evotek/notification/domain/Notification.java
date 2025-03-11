package com.evotek.notification.domain;

import java.util.Map;
import java.util.UUID;

import com.evotek.notification.domain.command.StoreNotificationCmd;
import com.evotek.notification.infrastructure.support.IdUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private UUID id;
    private String title;
    private String body;
    private String imageUrl;
    private String topic;
    private String token;
    private Map<String, String> data;

    public Notification(StoreNotificationCmd cmd) {
        this.id = IdUtils.nextId();
        this.title = cmd.getTitle();
        this.body = cmd.getBody();
        this.topic = cmd.getTopic();
        this.token = cmd.getToken();
        this.data = cmd.getData();
    }
}
