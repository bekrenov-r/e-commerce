package com.bekrenovr.ecommerce.apigateway.config;

import com.bekrenovr.ecommerce.apigateway.auth.JwtGrantedAuthoritiesConverter;
import com.bekrenovr.ecommerce.apigateway.auth.OptionalAuthAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        var postOrderEndpointAuthorizationManager =
                new OptionalAuthAuthorizationManager(new SimpleGrantedAuthority("CUSTOMER"));
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(spec -> spec.configurationSource(corsConfigurationSource))
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.GET, "/catalog/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/catalog/**").hasAuthority("EMPLOYEE")
                        .pathMatchers(HttpMethod.DELETE, "/catalog/**").hasAuthority("EMPLOYEE")
                        .pathMatchers(HttpMethod.POST,
                                "/users/registration/customer",
                                "/users/registration/customer/resend-email",
                                "/keycloak/realms/e-commerce/protocol/openid-connect/token",
                                "/keycloak/realms/e-commerce/users/enable").permitAll()
                        .pathMatchers(HttpMethod.POST, "/orders/").access(postOrderEndpointAuthorizationManager)
                        .pathMatchers(HttpMethod.GET, "/orders/{id}").hasAnyAuthority("CUSTOMER", "EMPLOYEE")
                        .pathMatchers(HttpMethod.GET, "/orders/customer").hasAuthority("CUSTOMER")
                        .pathMatchers(HttpMethod.GET, "/users/wishlist").hasAuthority("CUSTOMER")
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(spec -> spec.jwtAuthenticationConverter(new JwtGrantedAuthoritiesConverter()))
                ).securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }
}
