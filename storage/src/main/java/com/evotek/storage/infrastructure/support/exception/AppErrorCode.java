package com.evotek.storage.infrastructure.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum AppErrorCode {
    FILE_NOT_FOUND(1028, "File not found", HttpStatus.BAD_REQUEST),
    CANT_CREATE_DIR(1029, "Can't create directory", HttpStatus.BAD_REQUEST),
    INVALID_FILENAME(1030, "Invalid filename", HttpStatus.BAD_REQUEST),
    CANT_STORE_FILE(1031, "Can't store file", HttpStatus.BAD_REQUEST),
    CANT_HASH_FILE_NAME(1032, "Can't hash file name", HttpStatus.BAD_REQUEST),
    FILE_EXTENSION_NOT_ALLOWED(1033, "File extension not allowed", HttpStatus.BAD_REQUEST),
    FILE_TYPE_NOT_ALLOWED(1034, "File type not allowed", HttpStatus.BAD_REQUEST),
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
