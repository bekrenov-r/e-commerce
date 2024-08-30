package com.bekrenovr.ecommerce.keycloakserver.model;

import com.bekrenovr.ecommerce.common.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class EcommerceUser {
    private String username;
    private Set<Role> roles;
    private boolean enabled;
    private String firstName;
}
