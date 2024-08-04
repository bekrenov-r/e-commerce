package com.bekrenovr.ecommerce.users.dto.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
}
