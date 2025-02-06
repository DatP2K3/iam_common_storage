package com.evotek.iam.configuration;

import com.evo.common.config.FeignClientInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {
    @Bean
    public FeignClientInterceptor requestInterceptor() {
        return new FeignClientInterceptor();
    }
}