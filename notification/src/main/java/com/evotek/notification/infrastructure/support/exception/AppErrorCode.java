package com.evotek.notification.infrastructure.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum AppErrorCode {
    CANT_SEND_EMAIL(1030, "Can't send Email", HttpStatus.BAD_REQUEST),
    DEVICE_REGISTRATION_NOT_FOUND(1031, "Device registration not found", HttpStatus.NOT_FOUND),
    FIREBASE_SUBSCRIBE_TOPIC_FAILED(1032, "Failed to subscribe to topic", HttpStatus.INTERNAL_SERVER_ERROR),
    FIREBASE_SEND_NOTIFICATION_FAILED(1033, "Failed to send notification", HttpStatus.INTERNAL_SERVER_ERROR),
    NOTIFICATION_NOT_FOUND(1034, "Notification not found", HttpStatus.NOT_FOUND),
    DEVICE_REGISTRATION_ALREADY_EXISTS(1035, "Device registration already exists", HttpStatus.BAD_REQUEST),
    USER_TOPIC_NOT_FOUND(1036, "User topic not found", HttpStatus.NOT_FOUND),
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
