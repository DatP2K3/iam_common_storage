package com.evotek.iam.infrastructure.adapter.keycloak.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeycloakIdentityClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("KeycloakIdentityClientInterceptor apply");
    }
}
