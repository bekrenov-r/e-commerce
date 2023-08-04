package com.ecommerce.ordersdata.exception;

import com.ecommerce.common.ResponseSource;
import com.ecommerce.common.exception.ErrorDetail;
import com.ecommerce.common.exception.StandardResponseEntityExceptionHandler;
import feign.FeignException;
import feign.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class OrderResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {
    public OrderResponseEntityExceptionHandler(ResponseSource responseSource) {
        super(responseSource);
    }



    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorDetail> handleFeignExceptionNotFound(FeignException.NotFound ex, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                responseSource.toString()
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }
}
