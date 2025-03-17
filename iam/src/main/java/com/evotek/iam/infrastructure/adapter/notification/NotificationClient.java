package com.evotek.iam.infrastructure.adapter.notification;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.evotek.iam.infrastructure.adapter.notification.config.NotificationClientConfiguration;

@FeignClient(
        name = "notification-client",
        url = "${notification.url}",
        contextId = "notification-client",
        configuration = NotificationClientConfiguration.class,
        fallbackFactory = NotificationClientFallback.class)
public interface NotificationClient {
    @PostMapping(value = "/api/user-token/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void initUserTopic(@PathVariable("userId") UUID userId);
}
