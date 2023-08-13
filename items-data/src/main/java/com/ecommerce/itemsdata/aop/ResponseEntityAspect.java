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

    private final ResponseSource responseSource;

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
