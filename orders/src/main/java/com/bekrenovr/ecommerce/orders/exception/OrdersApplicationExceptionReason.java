package com.bekrenovr.ecommerce.orders.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrdersApplicationExceptionReason implements EcommerceApplicationExceptionReason {
    ORDER_NOT_FOUND("Order with id [%s] does not exist", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}
