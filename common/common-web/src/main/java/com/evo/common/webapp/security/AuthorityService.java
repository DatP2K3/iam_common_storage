package com.evo.common.webapp.security;

import com.evo.common.UserAuthority;

import java.util.UUID;

public interface AuthorityService {
    UserAuthority getUserAuthority(int userId);

    UserAuthority getUserAuthority(String username);

    UserAuthority getClientAuthority(String clientId);

}
