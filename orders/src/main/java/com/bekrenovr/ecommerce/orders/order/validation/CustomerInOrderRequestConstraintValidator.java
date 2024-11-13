package com.bekrenovr.ecommerce.orders.order.validation;

import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.orders.order.dto.CustomerRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomerInOrderRequestConstraintValidator
        implements ConstraintValidator<CustomerInOrderRequestConstraint, CustomerRequest> {

    private final Validator validator;

    @Override
    public boolean isValid(CustomerRequest customer, ConstraintValidatorContext context) {
        if(customerIsOptional()){
            return true;
        } else {
            boolean customerIsValid = this.validateCustomer(customer, context);
            return customer != null && customerIsValid;
        }
    }

    private boolean validateCustomer(CustomerRequest customer, ConstraintValidatorContext context) {
        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customer);
        if(!violations.isEmpty()) {
            context.disableDefaultConstraintViolation();
            violations.forEach(cv ->
                    context.buildConstraintViolationWithTemplate(cv.getMessage())
                            .addPropertyNode(cv.getPropertyPath().toString())
                            .addConstraintViolation()
            );
            return false;
        }
        return true;
    }

    private boolean customerIsOptional() {
        return AuthenticationUtil.requestHasAuthentication();
    }
}
