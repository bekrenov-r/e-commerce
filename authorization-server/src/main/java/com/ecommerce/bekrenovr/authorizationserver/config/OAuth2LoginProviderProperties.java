package com.ecommerce.bekrenovr.authorizationserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.security.oauth2.login")
@Data
public class OAuth2LoginProviderProperties {
    private Map<String, LoginProvider> provider;

    @Data
    public static class LoginProvider {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String[] scopes = new String[0];
        private String grantType;
    }

    public LoginProvider get(String providerId){
        for(var entry : provider.entrySet()){
            if(providerId.equalsIgnoreCase(entry.getKey())) return entry.getValue();
        }
        throw new RuntimeException("Configuration doesn't contain provider with id " + providerId);
    }

    public enum Providers {
        KEYCLOAK, GOOGLE
    }
}
