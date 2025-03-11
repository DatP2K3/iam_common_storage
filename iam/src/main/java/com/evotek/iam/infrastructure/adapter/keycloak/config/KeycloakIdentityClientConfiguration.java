package com.evotek.iam.infrastructure.adapter.keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakIdentityClientConfiguration {
    @Bean
    public KeycloakIdentityClientInterceptor requestInterceptor() {
        return new KeycloakIdentityClientInterceptor();
    }
}
