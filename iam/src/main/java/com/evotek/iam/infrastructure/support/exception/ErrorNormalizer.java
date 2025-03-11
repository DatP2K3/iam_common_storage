package com.evotek.iam.infrastructure.support.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorNormalizer {
    private final ObjectMapper objectMapper;
    private final Map<String, AuthErrorCode> errorCodeMap;

    public ErrorNormalizer() {
        objectMapper = new ObjectMapper();
        errorCodeMap = new HashMap<>();

        errorCodeMap.put("User exists with same username", AuthErrorCode.USER_EXISTED);
        errorCodeMap.put("User exists with same email", AuthErrorCode.EMAIL_EXISTED);
        errorCodeMap.put("User name is missing", AuthErrorCode.USERNAME_IS_MISSING);
    }

    public AuthException handleKeyCloakException(FeignException exception) {
        try {
            log.warn("Cannot complete request", exception);
            var response = objectMapper.readValue(exception.contentUTF8(), KeyCloakError.class);

            if (Objects.nonNull(response.getErrorMessage())
                    && Objects.nonNull(errorCodeMap.get(response.getErrorMessage()))) {
                return new AuthException(errorCodeMap.get(response.getErrorMessage()));
            }
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize content", e);
        }

        return new AuthException(AuthErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
