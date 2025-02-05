package com.evo.common;

import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    private final boolean isRoot;
    private final boolean isClient;
    private final Set<String> grantedPermissions;

    public UserAuthentication(Object principal,
                              Object credentials,
                              Collection<? extends GrantedAuthority> authorities,
                              boolean isRoot,
                              boolean isClient) {
        super(principal, credentials, authorities);
        this.isRoot = isRoot;
        this.isClient = isClient;
        this.grantedPermissions = CollectionUtils.isEmpty(authorities)
                ? Collections.emptySet()
                : authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public boolean isRoot() {
        return isRoot;
    }

    public boolean isClient() {
        return isClient;
    }

    public Set<String> getGrantedPermissions() {
        return this.grantedPermissions;
    }
}
