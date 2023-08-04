package com.ecommerce.userdata.exception;

import com.ecommerce.common.ResponseSource;
import com.ecommerce.common.exception.ErrorDetail;
import com.ecommerce.common.exception.StandardResponseEntityExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class CustomerResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {
    public CustomerResponseEntityExceptionHandler(ResponseSource responseSource) {
        super(responseSource);
    }

}
