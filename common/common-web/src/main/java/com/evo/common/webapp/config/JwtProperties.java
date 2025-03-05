package com.evo.common.webapp.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt")
public class JwtProperties
        extends OAuth2ResourceServerProperties.Jwt { // ánh xạ các URI JWKS (JSON Web Key Set) từ application.properties
    private Map<String, String> jwkSetUris;

    public Map<String, String> getJwkSetUris() {
        return jwkSetUris != null ? jwkSetUris : new HashMap<>();
    }
}
