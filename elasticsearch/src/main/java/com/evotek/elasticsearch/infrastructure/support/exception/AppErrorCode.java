package com.evotek.elasticsearch.infrastructure.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum AppErrorCode {
    USER_NOT_FOUND(1130, "User not found", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;

    AppErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
