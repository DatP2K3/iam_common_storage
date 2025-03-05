package com.evo.common.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientNoTokenConfiguration {
    @Bean
    public FeignNoTokenClientInterceptor requestInterceptor() {
        return new FeignNoTokenClientInterceptor();
    }
}