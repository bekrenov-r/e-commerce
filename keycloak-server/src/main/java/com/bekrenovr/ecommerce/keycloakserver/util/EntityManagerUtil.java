package com.bekrenovr.ecommerce.keycloakserver.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {
    private static final String PERSISTENCE_UNIT = "keycloak";
    public static EntityManager createEntityManager(){
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT)
                .createEntityManager();
    }
}
