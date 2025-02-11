package com.evotek.iam.configuration;

import com.evo.common.config.FeignClientInterceptor;
import feign.Feign;
import feign.Retryer;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public FeignClientInterceptor requestInterceptor() {
        return new FeignClientInterceptor();
    }
//
//    @Bean
//    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
//        return factory -> factory.configureDefault(id -> {
//            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
//                    .failureRateThreshold(50) // Tỉ lệ lỗi để mở circuit breaker (50%)
//                    .waitDurationInOpenState(Duration.ofSeconds(5)) // Thời gian chờ khi circuit breaker ở trạng thái mở
//                    .slidingWindowSize(10) // Kích thước cửa sổ để tính toán lỗi
//                    .build();
//
//            return new Resilience4JConfigBuilder(id)
//                    .circuitBreakerConfig(circuitBreakerConfig)
//                    .build();
//        });
//    }
//
//    @Bean
//    public RetryRegistry retryRegistry() {
//        io.github.resilience4j.retry.RetryConfig config = io.github.resilience4j.retry.RetryConfig.custom()
//                .maxAttempts(3)
//                .waitDuration(Duration.ofSeconds(2))
//                .retryExceptions(Exception.class)
//                .build();
//
//        return RetryRegistry.of(config);
//}
}