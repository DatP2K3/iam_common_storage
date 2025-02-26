package com.evotek.iam.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignStorageClientConfiguration {
    @Bean
    public FeignStorageClientInterceptor requestInterceptor() {
        return new FeignStorageClientInterceptor();
    }
}