package com.ecommerce.itemsdata.exception;

import com.ecommerce.itemsdata.util.dev.ErrorDetail;
import com.ecommerce.itemsdata.util.dev.StandardResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ItemResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {
    @ExceptionHandler(ItemApplicationException.class)
    public ResponseEntity<ErrorDetail> handleItemApplicationException(ItemApplicationException ex){
        super.logException(ex);
        ErrorDetail errorDetail = ErrorDetail.builder()
                .error(ex.getReason().getStatus())
                .statusCode(ex.getReason().getStatus().value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getReason().getStatus()).body(errorDetail);
    }
}


