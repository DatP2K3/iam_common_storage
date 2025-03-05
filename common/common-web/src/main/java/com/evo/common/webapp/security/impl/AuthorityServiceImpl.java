package com.evo.common.webapp.security.impl;

import com.evo.common.UserAuthority;
import com.evo.common.iam.client.IamClient;
import com.evo.common.iam.client.IamNoTokenClient;
import com.evo.common.webapp.security.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class AuthorityServiceImpl implements AuthorityService {
    private final IamClient iamClient;
    private final IamNoTokenClient iamNoTokenClient;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        return iamClient.getUserAuthority(userId).getData();
    }

    @Override
    public UserAuthority getUserAuthority(String username) {
        return iamClient.getUserAuthority(username).getData();
    }

    @Override
    public UserAuthority getClientAuthority(String clientId) {
        return iamNoTokenClient.getClientAuthority(clientId).getData();
    }
}
