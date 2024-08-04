package com.bekrenovr.ecommerce.users.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@AllArgsConstructor
@Getter
public enum UsersApplicationExceptionReason implements EcommerceApplicationExceptionReason {
    EMAIL_ALREADY_EXISTS("User with email [%s] already exists", CONFLICT);

    private final String message;
    private final HttpStatus status;
}
