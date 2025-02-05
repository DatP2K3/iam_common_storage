package com.evo.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {
    @Bean
    public FeignClientInterceptor requestInterceptor() {
        return new FeignClientInterceptor();
    }
}