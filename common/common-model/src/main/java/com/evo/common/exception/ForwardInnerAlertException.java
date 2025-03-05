package com.evo.common.exception;

import java.io.Serial;

import com.evo.common.dto.error.ErrorResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForwardInnerAlertException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ErrorResponse<Void> response;

    public ForwardInnerAlertException(ErrorResponse<Void> response) {
        this.response = response;
    }
}
