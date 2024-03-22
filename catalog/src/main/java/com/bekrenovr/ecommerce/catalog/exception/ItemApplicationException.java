package com.bekrenovr.ecommerce.catalog.exception;

import lombok.Getter;

@Getter
public class ItemApplicationException extends RuntimeException {

    private final ItemApplicationExceptionReason reason;
    private final String message;

    public ItemApplicationException(ItemApplicationExceptionReason reason) {
        this.message = reason.getMessage();
        this.reason = reason;
    }

    public ItemApplicationException(ItemApplicationExceptionReason reason, Object... args) {
        this.message = String.format(reason.getMessage(), args);
        this.reason = reason;
    }
}
