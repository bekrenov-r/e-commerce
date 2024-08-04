package com.bekrenovr.ecommerce.users.exception;

import com.bekrenovr.ecommerce.common.exception.ErrorDetail;
import com.bekrenovr.ecommerce.common.exception.StandardResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class UsersResponseEntityExceptionHandler extends StandardResponseEntityExceptionHandler {
    @ExceptionHandler(UsersApplicationException.class)
    public ResponseEntity<ErrorDetail> handleUsersApplicationException(UsersApplicationException ex) {
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                ex.getReason().getStatus(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, ex.getReason().getStatus());
    }
}
