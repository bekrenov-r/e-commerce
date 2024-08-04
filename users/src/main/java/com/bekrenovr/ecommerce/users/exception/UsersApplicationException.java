package com.bekrenovr.ecommerce.users.exception;

import lombok.Getter;

@Getter
public class UsersApplicationException extends RuntimeException {
    private final UsersApplicationExceptionReason reason;
    private final String message;

    public UsersApplicationException(UsersApplicationExceptionReason reason) {
        this.message = reason.getMessage();
        this.reason = reason;
    }

    public UsersApplicationException(UsersApplicationExceptionReason reason, Object... args) {
        this.message = String.format(reason.getMessage(), args);
        this.reason = reason;
    }
}
