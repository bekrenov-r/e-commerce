package com.bekrenovr.ecommerce.keycloakserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class EcommerceUser {
    private String username;
    private Set<Role> roles;
}
