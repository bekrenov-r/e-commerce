package com.bekrenovr.ecommerce.keycloakserver.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "User with username [%s] already exists";
    public UserAlreadyExistsException(String username) {
        super(MESSAGE_TEMPLATE.formatted(username));
    }
}
