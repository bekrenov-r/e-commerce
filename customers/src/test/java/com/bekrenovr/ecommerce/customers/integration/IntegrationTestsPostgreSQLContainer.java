package com.bekrenovr.ecommerce.customers.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public class IntegrationTestsPostgreSQLContainer extends PostgreSQLContainer<IntegrationTestsPostgreSQLContainer> {
    private static final String IMAGE_VERSION = "postgres:16.4";
    private static IntegrationTestsPostgreSQLContainer container;

    private IntegrationTestsPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static IntegrationTestsPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new IntegrationTestsPostgreSQLContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
    }
}
