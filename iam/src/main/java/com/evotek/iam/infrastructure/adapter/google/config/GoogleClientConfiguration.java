package com.evotek.iam.infrastructure.adapter.google.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleClientConfiguration {
    @Bean
    public GoogleClientInterceptor requestInterceptor() {
        return new GoogleClientInterceptor();
    }
}
