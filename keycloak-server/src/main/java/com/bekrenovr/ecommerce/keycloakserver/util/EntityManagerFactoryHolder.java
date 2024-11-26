package com.bekrenovr.ecommerce.keycloakserver.util;

import jakarta.persistence.EntityManagerFactory;

public class EntityManagerFactoryHolder {
    private static EntityManagerFactoryHolder INSTANCE;
    private static EntityManagerFactory entityManagerFactory;

    private EntityManagerFactoryHolder(EntityManagerFactory emf) {
        entityManagerFactory = emf;
    }

    public synchronized static void initialize(EntityManagerFactory entityManagerFactory) {
        if(INSTANCE == null) {
            INSTANCE = new EntityManagerFactoryHolder(entityManagerFactory);
        } else {
            throw new IllegalStateException(EntityManagerFactoryHolder.class.getSimpleName() + " instance was already initialized.");
        }
    }

    public synchronized static EntityManagerFactory getEntityManagerFactory() {
        if(INSTANCE == null) {
            throw new IllegalStateException(EntityManagerFactoryHolder.class.getSimpleName() + " instance was not initialized");
        }
        return entityManagerFactory;
    }
}
