package com.bekrenovr.ecommerce.common.exception;

import lombok.Getter;

@Getter
public class EcommerceApplicationException extends RuntimeException {
    private final EcommerceApplicationExceptionReason reason;
    private final String message;

    public EcommerceApplicationException(EcommerceApplicationExceptionReason reason) {
        this.message = reason.getMessage();
        this.reason = reason;
    }

    public EcommerceApplicationException(EcommerceApplicationExceptionReason reason, Object... args) {
        this.message = String.format(reason.getMessage(), args);
        this.reason = reason;
    }
}
