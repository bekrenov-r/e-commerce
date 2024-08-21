package com.bekrenovr.ecommerce.users.dto.request;

import com.bekrenovr.ecommerce.users.model.Person;
import lombok.Data;

@Data
public class CustomerRequest implements Person {
    private String firstName;
    private String lastName;
    private String email;
}
