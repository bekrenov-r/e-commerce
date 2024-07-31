package com.bekrenovr.ecommerce.keycloakserver.exception;

import com.bekrenovr.ecommerce.common.exception.ErrorDetail;
import com.bekrenovr.ecommerce.common.exception.StandardResponseEntityExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class KeycloakResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetail> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }
}
