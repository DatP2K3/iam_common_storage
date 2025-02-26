package com.evotek.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.evotek.iam", "com.evo.common"})
public class IamApplication {
	public static void main(String[] args) {
		SpringApplication.run(IamApplication.class, args);
	}
}
