package com.evo.common.webapp.security.impl;

import com.evo.common.UserAuthority;
import com.evo.common.iam.client.IamClient;
import com.evo.common.iam.client.IamNoTokenClient;
import com.evo.common.webapp.security.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class RemoteAuthorityServiceImpl implements AuthorityService {
    private final IamClient iamClient;
    private final IamNoTokenClient iamNoTokenClient;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        return null;
    }

    @Override
    public UserAuthority getUserAuthority(String username) {
        return null;
    }

    @Override
    public UserAuthority getClientAuthority(String clientId) {
        return null;
    }
}
