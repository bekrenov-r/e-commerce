package com.bekrenovr.ecommerce.keycloakserver.providers.restresource;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

@Slf4j
public class EcommerceUserRestResourceProviderFactory implements RealmResourceProviderFactory {
    private static final String PROVIDER_ID = "users";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new EcommerceUserRestResourceProvider(session);
    }

    @Override
    public void init(Config.Scope scope) {
        log.info("Initializing EcommerceUserRestResourceProviderFactory...");
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }
}
