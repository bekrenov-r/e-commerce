package com.ecommerce.bekrenovr.authorizationserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
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
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI openAPI() {
        Schema<?> accessTokenResponseSchema = new Schema<>()
                .addProperty("access_token", new StringSchema())
                .addProperty("expires_in", new StringSchema())
                .addProperty("refresh_token", new StringSchema())
                .addProperty("refresh_expires_in", new StringSchema())
                .addProperty("token_type", new StringSchema())
                .addProperty("scope", new StringSchema());
        return new OpenAPI()
                .info(new Info()
                        .title("Authorization Server API")
                        .description("OpenAPI definition for authorization service"))
                .servers(List.of(new Server().url(serverUrl())))
                .schema("AccessTokenResponseSchema", accessTokenResponseSchema);
    }

    private String serverUrl() {
        return "http://localhost:" + port + contextPath;
    }
}

