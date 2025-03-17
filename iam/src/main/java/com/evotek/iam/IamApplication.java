package com.evotek.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients(
        basePackages = {
            "com.evo.common.iam.client",
            "com.evo.common.storage.client",
            "com.evotek.iam.infrastructure.adapter.keycloak",
            "com.evotek.iam.infrastructure.adapter.notification",
            "com.evotek.iam.infrastructure.adapter.google"
        })
@SpringBootApplication(scanBasePackages = {"com.evotek.iam", "com.evo.common"})
public class IamApplication {
    public static void main(String[] args) {
        SpringApplication.run(IamApplication.class, args);
    }
}
