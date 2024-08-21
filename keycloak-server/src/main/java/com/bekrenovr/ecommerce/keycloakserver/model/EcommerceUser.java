package com.bekrenovr.ecommerce.keycloakserver.model;

import com.bekrenovr.ecommerce.common.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class EcommerceUser {
    private String username;
    private Set<Role> roles;
    private boolean enabled;
}
