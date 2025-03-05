package com.evo.common.webapp.security;

import java.util.UUID;

import com.evo.common.UserAuthority;

public interface AuthorityService {
    UserAuthority getUserAuthority(UUID userId);

    UserAuthority getUserAuthority(String username);

    UserAuthority getClientAuthority(String clientId);
}
