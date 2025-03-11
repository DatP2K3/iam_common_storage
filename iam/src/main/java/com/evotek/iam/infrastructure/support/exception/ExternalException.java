package com.evotek.iam.infrastructure.support.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalException extends RuntimeException {
    private final ExternalErrorCode externalErrorCode;

    public ExternalException(ExternalErrorCode externalErrorCode) {
        super(externalErrorCode.getMessage());
        this.externalErrorCode = externalErrorCode;
    }
}
