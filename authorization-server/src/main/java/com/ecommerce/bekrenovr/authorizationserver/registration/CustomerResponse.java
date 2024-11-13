package com.ecommerce.bekrenovr.authorizationserver.registration;

import com.bekrenovr.ecommerce.common.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CustomerResponse implements Person {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
