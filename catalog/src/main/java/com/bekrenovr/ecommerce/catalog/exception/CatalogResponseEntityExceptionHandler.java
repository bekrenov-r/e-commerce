package com.bekrenovr.ecommerce.catalog.exception;

import com.bekrenovr.ecommerce.common.exception.ErrorDetail;
import com.bekrenovr.ecommerce.common.exception.StandardResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CatalogResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {

    @ExceptionHandler(CatalogApplicationException.class)
    public ResponseEntity<ErrorDetail> handleItemApplicationException(CatalogApplicationException ex){
        super.logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                ex.getReason().getStatus(),
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getReason().getStatus()).body(errorDetail);
    }
}


