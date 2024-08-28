package com.bekrenovr.ecommerce.orders.validation;

import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.users.dto.request.CustomerRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomerInOrderRequestConstraintValidator
        implements ConstraintValidator<CustomerInOrderRequestConstraint, CustomerRequest> {

    @Override
    public boolean isValid(CustomerRequest customerRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(AuthenticationUtil.requestHasAuthentication()){
            return true;
        }
        return customerRequest != null;
    }
}
