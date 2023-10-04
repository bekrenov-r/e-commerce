package com.ecommerce.itemsdata.exception;

import com.ecommerce.itemsdata.util.dev.ErrorDetail;
import com.ecommerce.itemsdata.util.dev.ResponseSource;
import com.ecommerce.itemsdata.util.dev.StandardResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ItemResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {

    public ItemResponseEntityExceptionHandler(ResponseSource responseSource) {
        super(responseSource);
    }

    @Override
    public ResponseEntity<ErrorDetail> handleAllExceptions(Exception ex, WebRequest webRequest){
        this.logException(ex);
        return super.handleAllExceptions(ex, webRequest);
    }

    @ExceptionHandler(ItemApplicationException.class)
    public ResponseEntity<ErrorDetail> handleItemApplicationException(ItemApplicationException ex){
        this.logException(ex);
        ErrorDetail errorDetail = ErrorDetail.builder()
                .error(ex.getReason().getStatus())
                .statusCode(ex.getReason().getStatus().value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getReason().getStatus()).body(errorDetail);
    }

    private void logException(Exception ex){
        log.error(ex.getClass().getSimpleName() + ": ", ex);
    }
}


