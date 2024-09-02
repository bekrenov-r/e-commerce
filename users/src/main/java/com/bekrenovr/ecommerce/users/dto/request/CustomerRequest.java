package com.bekrenovr.ecommerce.users.dto.request;

import com.bekrenovr.ecommerce.users.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest implements Person {
    private String firstName;
    private String lastName;
    private String email;
}
