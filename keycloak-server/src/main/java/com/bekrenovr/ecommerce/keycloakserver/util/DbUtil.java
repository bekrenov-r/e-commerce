package com.bekrenovr.ecommerce.keycloakserver.util;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.bekrenovr.ecommerce.keycloakserver.providers.EcommerceUserStorageProviderConstants.*;

@Slf4j
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
}
