package com.evotek.iam.application.service.impl.query;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hibernate.query.sqm.tree.SqmNode.log;

import java.util.Date;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.evotek.iam.application.configuration.TokenProvider;
import com.evotek.iam.application.service.AuthServiceQuery;
import com.evotek.iam.domain.OauthClient;
import com.evotek.iam.domain.repository.OauthClientDomainRepository;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.RequiredArgsConstructor;

@Component("keycloakAuthQueryService")
@RequiredArgsConstructor
public class KeycloakAuthQueryService implements AuthServiceQuery {
    private final OauthClientDomainRepository oauthClientDomainRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getClientToken(String clientId, String clientSecret) {
        String token = redisTemplate.opsForValue().get("clientToken");
        if (token != null) {
            return token;
        }
        OauthClient oauthClient = oauthClientDomainRepository.findByClientId(clientId);
        if (!oauthClient.getClientSecret().equals(clientSecret)) {
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED);
        }
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(clientId)
                .issuer("evotek.iam.com")
                .issueTime(new Date())
                .jwtID(oauthClient.getClientId())
                .claim("userId", "common")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        RSASSASigner signer = new RSASSASigner(tokenProvider.getKeyPair().getPrivate());

        try {
            jwsObject.sign(signer);
            token = jwsObject.serialize();
            redisTemplate.opsForValue().set("clientToken", token, 30, MINUTES);
            return token;
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED);
        }
    }
}
