package com.evotek.iam.infrastructure.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum AuthErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    EMAIL_EXISTED(1008, "Email existed, please choose another one", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1009, "Username existed, please choose another one", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING(1010, "Please enter username", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1011, "User not existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1012, "Role not existed", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1013, "Invalid token", HttpStatus.BAD_REQUEST),
    LOGOUT_FAILED(1014, "Logout failed", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1015, "File not found", HttpStatus.BAD_REQUEST),
    INVALID_CLIENT_SECRET(1016, "Invalid client secret", HttpStatus.BAD_REQUEST),
    CLIENT_NOT_EXISTED(1017, "Client not existed", HttpStatus.BAD_REQUEST),
    CAN_NOT_CREATE_TOKEN(1018, "Can't create token", HttpStatus.BAD_REQUEST),
    PASSWORD_ALREADY_SET(1019, "Password already set", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;

    AuthErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
