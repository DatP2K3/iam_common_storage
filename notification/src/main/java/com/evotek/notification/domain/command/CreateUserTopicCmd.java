package com.evotek.notification.domain.command;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserTopicCmd {
    private UUID userId;
    private String topic;
    private boolean enabled;
}
