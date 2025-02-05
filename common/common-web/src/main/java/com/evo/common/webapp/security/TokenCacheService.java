package com.evo.common.webapp.security;

public interface TokenCacheService {  // Kiểm tra invalidToken
    String INVALID_REFRESH_TOKEN_CACHE = "invalid-refresh-token";
    String INVALID_TOKEN_CACHE = "invalid-access-token";
    boolean invalidToken(String token);

    boolean invalidRefreshToken(String refreshToken);

    boolean isExisted(String cacheName, String token);
}
