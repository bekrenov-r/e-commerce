package com.bekrenovr.ecommerce.common.exception;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
public class StandardResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAllExceptions(Exception ex, WebRequest webRequest){
        logException(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex, WebRequest webRequest){
        logException(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ProblemDetail> handleRestClientResponseException(RestClientResponseException ex) {
        logException(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(EcommerceApplicationException.class)
    public ResponseEntity<ProblemDetail> handleEcommerceApplicationException(EcommerceApplicationException ex) {
        logException(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getReason().getStatus(), ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handleFeignException(FeignException ex){
        logException(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.resolve(ex.status()), ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex){
        logException(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        logException(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        logException(ex);
        StringBuilder message = new StringBuilder("Errors:");
        for(FieldError error : ex.getFieldErrors()){
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
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail);
    }

    protected void logException(Exception ex){
        log.error(ex.getClass().getSimpleName() + ": ", ex);
    }

}