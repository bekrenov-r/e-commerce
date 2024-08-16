package com.bekrenovr.ecommerce.apigateway.config;

import com.bekrenovr.ecommerce.apigateway.util.JwtGrantedAuthoritiesConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(spec -> spec.configurationSource(corsConfigurationSource))
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.GET, "/catalog/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/catalog/**").hasAuthority("EMPLOYEE")
                        .pathMatchers(HttpMethod.DELETE, "/catalog/**").hasAuthority("EMPLOYEE")
                        .pathMatchers(HttpMethod.POST,
                                "/users/registration/customer",
                                "/keycloak/realms/e-commerce/protocol/openid-connect/token",
                                "/keycloak/realms/e-commerce/users/enable",
                                "/orders/").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(spec -> spec.jwtAuthenticationConverter(new JwtGrantedAuthoritiesConverter()))
                ).securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }
}
