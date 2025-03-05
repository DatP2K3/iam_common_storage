package com.evotek.storage.infrastructure.support.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    private AppErrorCode appErrorCode;
    public AppException(AppErrorCode appErrorCode) {
        super(appErrorCode.getMessage());
        this.appErrorCode = appErrorCode;
    }
}
