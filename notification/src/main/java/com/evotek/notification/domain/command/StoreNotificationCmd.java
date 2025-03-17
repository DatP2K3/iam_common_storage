package com.evotek.notification.domain.command;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreNotificationCmd {
    private String title;
    private String body;
    private String topic;
    private String token;
    private Map<String, String> data;
}
