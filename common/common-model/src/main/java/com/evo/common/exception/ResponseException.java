package com.evo.common.exception;


import com.evo.common.dto.error.ResponseError;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.Objects;

@Getter
public class ResponseException extends RuntimeException {
    private final ResponseError error;
    private final Object[] params;

    public ResponseException(ResponseError error) {
        this(error.getMessage(), null, error);
    }

    public ResponseException(String message, Throwable cause, ResponseError error) {
        this(message, cause, error, (Object) null);
    }

    public ResponseException(
            String message, Throwable cause, ResponseError error, Object... params) {
        super(
                Objects.nonNull(message)
                        ? MessageFormat.format(message, params)
                        : error.getMessage(),
                cause);
        this.error = error;
        this.params = params == null ? new Object[0] : params;
    }

    public ResponseException(ResponseError error, Object... params) {
        this(error.getMessage(), null, error, params);
    }

    public ResponseException(String message, ResponseError error) {
        this(message, null, error);
    }

    public ResponseException(String message, ResponseError error, Object... params) {
        this(message, null, error, params);
    }

}
