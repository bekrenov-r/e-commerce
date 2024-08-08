package com.bekrenovr.ecommerce.keycloakserver.util;

import com.bekrenovr.ecommerce.keycloakserver.config.properties.ApplicationPropertiesHolder;
import com.bekrenovr.ecommerce.keycloakserver.config.properties.SecondaryDatasourceConfigProperties;
import org.keycloak.component.ComponentModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.bekrenovr.ecommerce.keycloakserver.providers.userstorage.EcommerceUserStorageProviderConstants.*;

public class DbUtil {
    public static Connection getConnection(ComponentModel config) {
        try {
            return DriverManager.getConnection(
                    config.get(CONFIG_KEY_JDBC_URL),
                    config.get(CONFIG_KEY_DB_USERNAME),
                    config.get(CONFIG_KEY_DB_PASSWORD)
            );
        } catch(SQLException ex) {
            throw new RuntimeException("Error connecting to database", ex);
        }
    }

    public static Connection getConnection() {
        SecondaryDatasourceConfigProperties properties =
                ApplicationPropertiesHolder.getSecondaryDatasourceConfigProperties();
        try {
            return DriverManager.getConnection(
                    properties.getUrl(),
                    properties.getUsername(),
                    properties.getPassword()
            );
        } catch(SQLException ex) {
            throw new RuntimeException("Error connecting to database", ex);
        }
    }
}
