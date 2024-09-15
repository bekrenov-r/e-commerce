package com.bekrenovr.ecommerce.common.exception;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Log4j2
public class StandardResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleAllExceptions(Exception ex, WebRequest webRequest){
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetail> handleIllegalArgument(IllegalArgumentException ex, WebRequest webRequest){
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ErrorDetail> handleRestClientResponseException(RestClientResponseException ex) {
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                ex.getStatusCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, ex.getStatusCode());
    }

    @ExceptionHandler(EcommerceApplicationException.class)
    public ResponseEntity<ErrorDetail> handleEcommerceApplicationException(EcommerceApplicationException ex) {
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                ex.getReason().getStatus(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, ex.getReason().getStatus());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetail> handleFeignException(FeignException ex){
        HttpStatus status = HttpStatus.resolve(ex.status());
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                status,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetail> handleConstraintViolationException(ConstraintViolationException ex){
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        logException(ex);
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                status,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetail, status);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        logException(ex);
        StringBuilder message = new StringBuilder("Errors:");
        for(FieldError error : ex.getFieldErrors()){
            System.out.println(error.getObjectName());
            message
                    .append(" [")
                    .append(error.getObjectName())
                    .append(".")
                    .append(error.getField())
                    .append(" ")
                    .append(error.getDefaultMessage())
                    .append(", rejected value: ")
                    .append(error.getRejectedValue())
                    .append("]");
        }
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                status,
                message.toString()
        );
        return new ResponseEntity<>(errorDetail, status);
    }

    protected void logException(Exception ex){
        log.error(ex.getClass().getSimpleName() + ": ", ex);
    }

}