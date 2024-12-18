package com.ecommerce.bekrenovr.authorizationserver.user;

import com.bekrenovr.ecommerce.common.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeycloakUserResponse implements Person {
    private String firstName;
    private String email;

    @Override
    public String getLastName() {
        return null;
    }
}
