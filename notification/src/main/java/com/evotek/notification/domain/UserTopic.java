package com.evotek.notification.domain;

import java.util.UUID;

import com.evotek.notification.domain.command.CreateUserTopicCmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTopic {
    private UUID id;
    private UUID userId;
    private String topic;
    private boolean enabled;

    public UserTopic(CreateUserTopicCmd cmd) {
        this.userId = cmd.getUserId();
        this.topic = cmd.getTopic();
        this.enabled = cmd.isEnabled();
    }
}
