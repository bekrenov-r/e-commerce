package com.bekrenovr.ecommerce.catalog.exception;

import com.bekrenovr.ecommerce.common.exception.ErrorDetail;
import com.bekrenovr.ecommerce.common.exception.StandardResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ItemResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {

    @ExceptionHandler(ItemApplicationException.class)
    public ResponseEntity<ErrorDetail> handleItemApplicationException(ItemApplicationException ex){
        super.logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                ex.getReason().getStatus(),
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getReason().getStatus()).body(errorDetail);
    }
}


