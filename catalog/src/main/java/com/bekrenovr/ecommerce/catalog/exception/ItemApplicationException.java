package com.bekrenovr.ecommerce.catalog.exception;

import lombok.Getter;

@Getter
public class ItemApplicationException extends RuntimeException {

    private ItemApplicationExceptionReason reason;
    private String message;

    public ItemApplicationException(ItemApplicationExceptionReason reason) {
        this.message = reason.getMessage();
    }

    public ItemApplicationException(ItemApplicationExceptionReason reason, Object... args) {
        this.message = String.format(reason.getMessage(), args);
        this.reason = reason;
    }
}
