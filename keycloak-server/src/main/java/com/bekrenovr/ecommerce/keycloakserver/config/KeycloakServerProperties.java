package com.bekrenovr.ecommerce.keycloakserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.server")
@Getter
@Setter
public class KeycloakServerProperties {
    private String contextPath = "";
    private String realmImportFile;
    private AdminUser adminUser = new AdminUser();

    @Getter
    @Setter
    public static class AdminUser {
        private String username;
        private String password;
    }
}