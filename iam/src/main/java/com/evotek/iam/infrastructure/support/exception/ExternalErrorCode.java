package com.evotek.iam.infrastructure.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ExternalErrorCode {
    FAIL_TO_SEND_MAIL(10019, "Fail to send mail", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;

    ExternalErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
