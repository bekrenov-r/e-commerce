package com.bekrenovr.ecommerce.reviews.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewsApplicationExceptionReason implements EcommerceApplicationExceptionReason {
    CANNOT_CREATE_REVIEW("You must have completed order for this item to create review", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
