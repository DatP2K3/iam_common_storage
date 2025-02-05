package com.evo.common.webapp.security.impl;

import com.evo.common.webapp.config.JwtProperties;
import com.evo.common.webapp.security.TokenCacheService;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import static org.bouncycastle.asn1.x509.ObjectDigestInfo.publicKey;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenCacheServiceImpl implements TokenCacheService {
    private final RedisTemplate redisTemplate;

    @Override
    public boolean invalidToken(String token) {
        Double score = redisTemplate.opsForZSet().score(INVALID_TOKEN_CACHE, token);
        return score != null && score > System.currentTimeMillis();
    }

    @Override
    public boolean invalidRefreshToken(String refreshToken) {
        Double score = redisTemplate.opsForZSet().score(INVALID_REFRESH_TOKEN_CACHE, refreshToken);
        return score != null && score > System.currentTimeMillis();
    }

    @Override
    public boolean isExisted(String cacheName, String token) {
        if (cacheName.equals(INVALID_TOKEN_CACHE)) {
            return invalidToken(token);
        } else if (cacheName.equals(INVALID_REFRESH_TOKEN_CACHE)) {
            return invalidRefreshToken(token);
        }
        return false;
    }
}
