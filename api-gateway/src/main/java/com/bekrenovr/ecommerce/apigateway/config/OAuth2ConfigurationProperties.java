package com.bekrenovr.ecommerce.apigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.util.stream.Stream;

@Data
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class OAuth2ConfigurationProperties {
    private IssuerProperties[] issuers;

    @Data
    public static class IssuerProperties {
        private String uri;
        private String usernameJsonPath;
        private String rolesJsonPath;
        private String[] defaultRoles = new String[0];

        public boolean hasDefaultRoles() {
            return defaultRoles.length > 0;
        }
    }

    public IssuerProperties get(URI issuerUri) {
        return Stream.of(issuers)
                .filter(iss -> iss.getUri().equals(issuerUri.toString()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No configuration provided for issuer %s".formatted(issuerUri.toString())));
    }
}
