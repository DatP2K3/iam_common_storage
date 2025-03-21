package com.evo.common.webapp.security.impl;

import org.springframework.stereotype.Service;

import com.evo.common.UserAuthority;
import com.evo.common.iam.client.IamClient;
import com.evo.common.iam.client.IamNoTokenClient;
import com.evo.common.webapp.security.AuthorityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoteAuthorityServiceImpl implements AuthorityService {
    private final IamClient iamClient;
    private final IamNoTokenClient iamNoTokenClient;

    @Override
    public UserAuthority getUserAuthority(String username) {
        return iamClient.getUserAuthority(username).getData();
    }

    @Override
    public UserAuthority getClientAuthority(String clientId) {
        return iamNoTokenClient.getClientAuthority(clientId).getData();
    }
}
