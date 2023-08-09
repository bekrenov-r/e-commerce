package com.ecommerce.itemsdata.exception;

import lombok.Getter;

import java.util.Formatter;

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
