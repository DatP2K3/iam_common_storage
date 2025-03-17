package com.evo.common.webapp.security;

import com.evo.common.UserAuthority;

public interface AuthorityService {
    UserAuthority getUserAuthority(String username);

    UserAuthority getClientAuthority(String clientId);
}
