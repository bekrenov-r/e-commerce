package com.bekrenovr.ecommerce.keycloakserver;

import com.bekrenovr.ecommerce.keycloakserver.config.properties.KeycloakServerProperties;
import com.bekrenovr.ecommerce.keycloakserver.config.properties.SecondaryDatasourceConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
@EnableConfigurationProperties({ KeycloakServerProperties.class, SecondaryDatasourceConfigProperties.class })
public class KeycloakServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(KeycloakServerApplication.class, args);
	}
}
