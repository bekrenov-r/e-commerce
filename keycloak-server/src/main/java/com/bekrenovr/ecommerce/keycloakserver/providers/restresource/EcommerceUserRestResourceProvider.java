package com.bekrenovr.ecommerce.keycloakserver.providers.restresource;

import com.bekrenovr.ecommerce.keycloakserver.web.EcommerceUserEndpoint;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class EcommerceUserRestResourceProvider implements RealmResourceProvider {
    private final KeycloakSession session;

    public EcommerceUserRestResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return new EcommerceUserEndpoint(session);
    }

    @Override
    public void close() {

    }
}
