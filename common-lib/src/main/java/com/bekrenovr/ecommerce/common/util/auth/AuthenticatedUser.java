package com.bekrenovr.ecommerce.common.util.auth;

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
}
