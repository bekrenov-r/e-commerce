package com.bekrenovr.ecommerce.orders.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomerInOrderRequestConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerInOrderRequestConstraint {
    String message() default "must be present if request is not authenticated";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
