package com.bekrenovr.ecommerce.keycloakserver.config.properties;

public class ApplicationPropertiesHolder {
    private static KeycloakServerProperties keycloakServerProperties;
    private static SecondaryDatasourceConfigProperties secondaryDatasourceConfigProperties;

    public static KeycloakServerProperties getKeycloakServerProperties() {
        return keycloakServerProperties;
    }

    public static void setKeycloakServerProperties(KeycloakServerProperties serverProperties) {
        keycloakServerProperties = serverProperties;
    }

    public static SecondaryDatasourceConfigProperties getSecondaryDatasourceConfigProperties() {
        return secondaryDatasourceConfigProperties;
    }

    public static void setSecondaryDatasourceConfigProperties(SecondaryDatasourceConfigProperties properties) {
        secondaryDatasourceConfigProperties = properties;
    }
}
