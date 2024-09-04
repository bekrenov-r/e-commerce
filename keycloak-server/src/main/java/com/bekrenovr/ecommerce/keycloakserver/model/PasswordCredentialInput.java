package com.bekrenovr.ecommerce.keycloakserver.model;

import lombok.AllArgsConstructor;
import org.keycloak.credential.CredentialInput;

@AllArgsConstructor
public class PasswordCredentialInput implements CredentialInput {
    private String username;
    private String password;

    @Override
    public String getCredentialId() {
        return username;
    }

    @Override
    public String getType() {
        return "password";
    }

    @Override
    public String getChallengeResponse() {
        return password;
    }
}
