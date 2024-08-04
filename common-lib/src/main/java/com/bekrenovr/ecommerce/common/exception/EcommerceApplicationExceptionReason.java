package com.bekrenovr.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

public interface EcommerceApplicationExceptionReason {
    String getMessage();
    HttpStatus getStatus();
}
