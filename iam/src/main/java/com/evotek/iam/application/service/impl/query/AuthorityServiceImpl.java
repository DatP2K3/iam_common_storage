package com.evotek.iam.application.service.impl.query;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.evo.common.UserAuthority;
import com.evo.common.webapp.security.AuthorityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class AuthorityServiceImpl implements AuthorityService {
    private final UserQueryServiceImpl userQueryService;

    @Override
    public UserAuthority getUserAuthority(String username) {
        return userQueryService.getUserAuthority(username);
    }

    @Override
    public UserAuthority getClientAuthority(String clientId) {
        return userQueryService.getClientAuthority(clientId);
    }
}
