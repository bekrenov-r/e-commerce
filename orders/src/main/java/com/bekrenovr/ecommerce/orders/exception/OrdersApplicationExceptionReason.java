package com.bekrenovr.ecommerce.orders.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrdersApplicationExceptionReason implements EcommerceApplicationExceptionReason {
    ORDER_NOT_FOUND("Order with id [%s] does not exist", HttpStatus.NOT_FOUND),
    NON_EXISTENT_ITEMS_IN_ORDER("Order request contains non-existent items", HttpStatus.BAD_REQUEST),
    SIZE_IS_UNAVAILABLE("Size [%s] is unavailable for item with id [%s]", HttpStatus.BAD_REQUEST),
    QUANTITY_IS_UNAVAILABLE("Requested quantity is unavailable for item [%s] with size [%s]", HttpStatus.BAD_REQUEST),
    CUSTOMER_IS_NOT_ORDER_OWNER("Customer with username [%s] is not an owner of requested order", HttpStatus.FORBIDDEN),
    ITEM_ALREADY_IN_CART("Item with id [%s] is already added to cart", HttpStatus.CONFLICT),
    CART_ENTRY_NOT_FOUND("Cart does not contain item entry with id [%s]", HttpStatus.NOT_FOUND),
    CANNOT_CANCEL_ORDER("Cannot cancel order with status [%s]", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
