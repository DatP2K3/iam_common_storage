package com.evo.common.config;

import org.springframework.context.annotation.Bean;

public class FeignClientNoTokenConfiguration {
    @Bean
    public FeignNoTokenClientInterceptor requestInterceptor() {
        return new FeignNoTokenClientInterceptor();
    }
}