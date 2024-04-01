package com.bekrenovr.ecommerce.catalog.exception;

import lombok.Getter;

@Getter
public class CatalogApplicationException extends RuntimeException {

    private final CatalogApplicationExceptionReason reason;
    private final String message;

    public CatalogApplicationException(CatalogApplicationExceptionReason reason) {
        this.message = reason.getMessage();
        this.reason = reason;
    }

    public CatalogApplicationException(CatalogApplicationExceptionReason reason, Object... args) {
        this.message = String.format(reason.getMessage(), args);
        this.reason = reason;
    }
}
