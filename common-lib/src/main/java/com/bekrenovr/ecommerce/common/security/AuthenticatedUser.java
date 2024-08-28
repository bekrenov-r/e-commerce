package com.bekrenovr.ecommerce.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
public class AuthenticatedUser {
    private String username;
    private Collection<String> authorities;

    public boolean hasRole(Role role){
        return authorities.stream()
                .map(Role::valueOf)
                .anyMatch(role::equals);
    }
}
