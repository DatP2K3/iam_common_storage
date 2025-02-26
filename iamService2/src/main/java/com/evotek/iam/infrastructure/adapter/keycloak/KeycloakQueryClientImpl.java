package com.evotek.iam.infrastructure.adapter.keycloak;

import com.evotek.iam.application.dto.request.identityKeycloak.GetTokenRequest;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.infrastructure.support.exception.ErrorNormalizer;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakQueryClientImpl implements KeycloakQueryClient {
    private final KeycloakIdentityClient keycloakIdentityClient;
    private final ErrorNormalizer errorNormalizer;
    @Value("${idp.client-id}")
    private String clientId;
    @Value("${idp.client-secret}")
    private String clientSecret;

    @Override
    public String getClientToken() {
        try {
            TokenDTO tokenDTO = keycloakIdentityClient.getToken(GetTokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());
            return tokenDTO.getAccessToken();
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }
}
