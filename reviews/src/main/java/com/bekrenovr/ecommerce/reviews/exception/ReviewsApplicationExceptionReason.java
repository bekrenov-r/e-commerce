package com.bekrenovr.ecommerce.reviews.exception;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationExceptionReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewsApplicationExceptionReason implements EcommerceApplicationExceptionReason {
    CANNOT_CREATE_REVIEW("You must have completed order for this item to create review", HttpStatus.BAD_REQUEST),
    REVIEW_ALREADY_EXISTS("You already have review for this item", HttpStatus.CONFLICT),
    REVIEW_NOT_FOUND("Review with id [%s] does not exist", HttpStatus.NOT_FOUND),
    NOT_REVIEW_OWNER("You are not owner of this review", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus status;
}
