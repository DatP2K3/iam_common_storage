package com.evotek.iam.application.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {
    private boolean keycloakEnabled;
}
