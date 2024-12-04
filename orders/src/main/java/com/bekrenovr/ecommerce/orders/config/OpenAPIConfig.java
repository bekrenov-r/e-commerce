package com.bekrenovr.ecommerce.orders.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {
    @Value("${server.port}")
    private int port;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orders API")
                        .description("OpenAPI definition for orders service")
                        .version("0.1"))
                .servers(List.of(new Server().url(serverUrl())));
    }

    private String serverUrl() {
        return "http://localhost:" + port;
    }
}

