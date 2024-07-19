package com.bekrenovr.ecommerce.keycloakserver.model;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.storage.adapter.AbstractUserAdapter;

public class EcommerceUser extends AbstractUserAdapter {
    private String username;

    public EcommerceUser(KeycloakSession session, RealmModel realm, ComponentModel providerModel, String username) {
        super(session, realm, providerModel);
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return new LegacyUserCredentialManager(session, realm, this);
    }
}
