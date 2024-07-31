package com.bekrenovr.ecommerce.keycloakserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.secondary-datasource")
@Getter
@Setter
public class SecondaryDatasourceConfigProperties {
    private String url;
    private String username;
    private String password;
}
