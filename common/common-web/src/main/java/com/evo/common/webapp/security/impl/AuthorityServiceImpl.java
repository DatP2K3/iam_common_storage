package com.evo.common.webapp.security.impl;

import com.evo.common.UserAuthority;
import com.evo.common.client.iam.IamClient;
import com.evo.common.client.iam.IamNoTokenClient;
import com.evo.common.webapp.security.AuthorityService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class AuthorityServiceImpl implements AuthorityService {
    private final IamClient iamClient;
    private final IamNoTokenClient iamNoTokenClient;
    @Value("${oauth.client.iam.client-id}")
    private String clientId;

    @Value("${oauth.client.iam.client-secret}")
    private String clientSecret;

    @Override
    public UserAuthority getUserAuthority(int userId) {
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
