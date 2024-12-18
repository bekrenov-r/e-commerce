package com.bekrenovr.ecommerce.keycloakserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EcommerceUserResponse {
    private String email;
    private String firstName;
}
