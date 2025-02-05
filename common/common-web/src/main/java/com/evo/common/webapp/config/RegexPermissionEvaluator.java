package com.evo.common.webapp.config;

import com.evo.common.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RegexPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String requiredPermission = permission.toString();
        if (!(authentication instanceof UserAuthentication userAuthentication)) {
            throw new RuntimeException("NOT_SUPPORTED_AUTHENTICATION");
        }

        System.out.println(userAuthentication.getGrantedPermissions());
        System.out.println(requiredPermission);

        if (userAuthentication.isRoot()) {
            System.out.println("Root user, granting all permissions.");
            return true;
        }

        if (userAuthentication.isClient()) {
            System.out.println("Client user, granting all permissions.");
            return true;
        }

        return userAuthentication.getGrantedPermissions().stream()
                .map(Pattern::compile)
                .anyMatch(pattern -> pattern.matcher(requiredPermission).matches());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}
