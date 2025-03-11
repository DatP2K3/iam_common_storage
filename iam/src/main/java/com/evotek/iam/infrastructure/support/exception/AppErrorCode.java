package com.evotek.iam.infrastructure.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum AppErrorCode {
    FILE_NOT_FOUND(1015, "File not found", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1020, "Permission not found", HttpStatus.BAD_REQUEST),
    PERMISSION_ASSIGNED_TO_ROLE(1021, "Permission assigned to role", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1022, "Role not found", HttpStatus.BAD_REQUEST),
    ROLE_PERMISSION_NOT_EXISTED(1023, "Role permission not existed", HttpStatus.BAD_REQUEST),
    USER_ACTIVITY_LOG_NOT_FOUND(1024, "User activity log not found", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1025, "User not found", HttpStatus.BAD_REQUEST),
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
