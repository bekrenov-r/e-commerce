package com.bekrenovr.ecommerce.keycloakserver.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.postgresql")
@Getter
@Setter
public class PostgreSQLProperties {
    private String url;
    private String username;
    private String password;
    private String databaseName;
}
