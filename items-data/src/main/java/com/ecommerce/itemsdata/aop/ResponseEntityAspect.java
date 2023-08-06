package com.ecommerce.itemsdata.aop;

import com.ecommerce.itemsdata.dto.response.ItemResponse;
import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.util.dev.ResponseSource;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class ResponseEntityAspect {

    // todo: create advice for adding response source header to ResponseEntity
    private final ResponseSource responseSource;

    @Around("execution(* com.ecommerce.itemsdata.controller.ItemController.getAll*(..))")
    public ResponseEntity<List<ItemResponse>> addContentLength(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity<List<ItemResponse>> responseEntity = (ResponseEntity<List<ItemResponse>>) joinPoint.proceed();
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(responseEntity.getHeaders());
        headers.add("Items-Found", String.valueOf(responseEntity.getBody().size()));
        return ResponseEntity
                .status(responseEntity.getStatusCode())
                .headers(headers)
                .body(responseEntity.getBody());
    }

    @Around("execution(org.springframework.http.ResponseEntity *(..))")
    public ResponseEntity<?> addResponseSource(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) joinPoint.proceed();
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(responseEntity.getHeaders());
        headers.add("Response-Source", responseSource.toString());
        return ResponseEntity
                .status(responseEntity.getStatusCode())
                .headers(headers)
                .body(responseEntity.getBody());
    }

}
