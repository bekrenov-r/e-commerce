package com.ecommerce.userdata.aop;

import com.ecommerce.userdata.entity.Customer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CustomerValidationAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void hasGetMapping(){}
    @Pointcut("execution(* com.ecommerce.userdata.controller.CustomerController.*(*))")
    public void hasOneArg(){}

    @Before("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void validateIdOnPutMapping(JoinPoint joinPoint){
        Customer customer = (Customer) joinPoint.getArgs()[0];

        if(customer.getId() == null){
            throw new IllegalArgumentException("Customer id can't be null for PUT method");
        } else if(customer.getId() <= 0){
            throw new IllegalArgumentException("Customer id must be greater than 0, provided: " + customer.getId());
        }
        if(customer.getAddress().getId() == null){
            throw new IllegalArgumentException("Address id can't be null for PUT method");
        } else if(customer.getAddress().getId() <= 0){
            throw new IllegalArgumentException("Address id must be greater than 0, provided: " + customer.getAddress().getId());
        }
    }

    @Before("hasGetMapping() && hasOneArg()")
    public void validateIdOnGetMapping(JoinPoint joinPoint){
        int id = (int) joinPoint.getArgs()[0];
        if(id <= 0){
            throw new IllegalArgumentException("Id must be greater than 0, provided: " + id);
        }
    }

}
