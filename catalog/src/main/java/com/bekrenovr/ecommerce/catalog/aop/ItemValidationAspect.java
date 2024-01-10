package com.bekrenovr.ecommerce.catalog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class ItemValidationAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void hasGetMapping(){}
    @Pointcut("execution(* com.bekrenovr.ecommerce.catalog.controller.ItemController.*(java.lang.Integer))")
    public void hasIntegerParam(){}

    @Pointcut("execution(* com.bekrenovr.ecommerce.catalog.controller.ItemController.*(java.util.List))")
    public void hasListParam(){}

    /*@Before("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void validateIdOnPutMapping(JoinPoint joinPoint){
        Item item = (Item) joinPoint.getArgs()[0];

        if(item.getId() == null){
            throw new IllegalArgumentException("Id can't be null for PUT method");
        } else if(item.getId() <= 0){
            throw new IllegalArgumentException("Id must be greater than 0, provided: " + item.getId());
        }
    }*/

    @Before("hasGetMapping() && hasIntegerParam()")
    public void validateIdOnGetMapping(JoinPoint joinPoint){
        int id = (int) joinPoint.getArgs()[0];
        if(id <= 0){
            throw new IllegalArgumentException("Id must be greater than 0, provided: " + id);
        }
    }

    @Before("hasGetMapping() && hasListParam()")
    public void validateIdsOnGetMapping(JoinPoint joinPoint){
        List<Integer> ids = (List<Integer>) joinPoint.getArgs()[0];
        ids.forEach(id -> {
            if(id == null)
                throw new IllegalArgumentException("Id can't be null for GET method");
            else if(id <= 0)
                throw new IllegalArgumentException("Id must be greater than 0, provided: " + id);
        });
    }

}
