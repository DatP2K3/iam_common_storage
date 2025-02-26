package com.evo.common.config;

import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {
    @Bean
    public FeignClientInterceptor requestInterceptor() {
        return new FeignClientInterceptor();
    }
}