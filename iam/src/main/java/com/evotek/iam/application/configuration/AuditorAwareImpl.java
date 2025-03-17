package com.evotek.iam.application.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Value("${auth.keycloak-enabled}")
    private boolean keycloakEnabled;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        if (keycloakEnabled) {
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            return Optional.of(username);
        }
        return Optional.of(authentication.getName());
    }
}
