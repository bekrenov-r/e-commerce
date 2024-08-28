package com.bekrenovr.ecommerce.users.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UsersApplicationExceptionReason implements EcommerceApplicationExceptionReason {
    EMAIL_ALREADY_EXISTS("User with email [%s] already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND_BY_EMAIL("User with email [%s] does not exist", HttpStatus.NOT_FOUND),
    CUSTOMER_IS_NOT_ORDER_OWNER("Customer with username [%s] is not an owner of requested order", HttpStatus.FORBIDDEN),
    ITEM_ALREADY_ON_WISH_LIST("Item with id [%s] is already on wish list", HttpStatus.CONFLICT),
    WISH_LIST_ITEM_NOT_FOUND("Item with id [%s] is not on wish list for current user", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}
