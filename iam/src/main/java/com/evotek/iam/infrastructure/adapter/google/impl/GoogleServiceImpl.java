package com.evotek.iam.infrastructure.adapter.google.impl;

import com.evotek.iam.infrastructure.adapter.google.GoogleUserClient;
import org.springframework.stereotype.Service;

import com.evotek.iam.application.dto.request.ExchangeTokenRequest;
import com.evotek.iam.application.dto.response.OutboundUserDTO;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.infrastructure.adapter.google.GoogleIdentityClient;
import com.evotek.iam.infrastructure.adapter.google.GoogleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {
    private final GoogleIdentityClient googleIdentityClient;
    private final GoogleUserClient googleUserClient;

    @Override
    public TokenDTO exchangeToken(ExchangeTokenRequest request) {
        return googleIdentityClient.exchangeToken(request);
    }

    @Override
    public OutboundUserDTO getUserInfo(String accessToken) {
        return googleUserClient.getUserInfo("json", accessToken);
    }
}
