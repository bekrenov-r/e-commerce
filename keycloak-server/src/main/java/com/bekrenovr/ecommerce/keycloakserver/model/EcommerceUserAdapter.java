package com.bekrenovr.ecommerce.keycloakserver.model;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import java.util.Set;
import java.util.stream.Collectors;

public class EcommerceUserAdapter extends AbstractUserAdapter {
    private EcommerceUser user;

    public EcommerceUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel componentModel, EcommerceUser user) {
        super(session, realm, componentModel);
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    protected boolean appendDefaultRolesToRoleMappings() {
        return false;
    }

    @Override
    protected Set<RoleModel> getRoleMappingsInternal() {
        return user.getRoles().stream()
                .map(Role::name)
                .map(realm::getRole)
                .collect(Collectors.toSet());
    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return new LegacyUserCredentialManager(session, realm, this);
    }
}
