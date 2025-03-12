package com.evotek.iam.infrastructure.adapter.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationClientConfiguration {
    @Bean
    public NotificationClientInterceptor requestInterceptor() {
        return new NotificationClientInterceptor();
    }
}
