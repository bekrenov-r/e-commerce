package com.bekrenovr.ecommerce.keycloakserver.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum KeycloakApplicationExceptionReason implements EcommerceApplicationExceptionReason {
    USER_ALREADY_EXISTS("User with username [%s] already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND("User with username [%s] does not exist", HttpStatus.NOT_FOUND),
    ACTIVATION_TOKEN_NOT_FOUND("Activation token [%s] does not exist", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}
