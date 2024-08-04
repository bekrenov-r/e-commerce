package com.bekrenovr.ecommerce.users.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerRegistrationRequest extends CustomerRequest {
    private String password;
}
