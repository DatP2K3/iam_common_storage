package com.evo.common.webapp.security.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.evo.common.webapp.security.TokenCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenCacheServiceImpl implements TokenCacheService {
    private final RedisTemplate<String, String> redisTemplate;

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
