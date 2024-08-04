package com.bekrenovr.ecommerce.keycloakserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivationToken {
    private String username;
    private String token;
}
