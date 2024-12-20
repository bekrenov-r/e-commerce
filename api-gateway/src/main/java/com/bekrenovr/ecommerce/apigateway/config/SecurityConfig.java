package com.bekrenovr.ecommerce.apigateway.config;

import com.bekrenovr.ecommerce.apigateway.auth.JwtAuthenticationTokenConverter;
import com.bekrenovr.ecommerce.apigateway.auth.OptionalAuthAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerReactiveAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final OAuth2ConfigurationProperties authConfigurationProperties;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        var postOrderEndpointAuthorizationManager =
                new OptionalAuthAuthorizationManager("CUSTOMER");
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(spec -> spec.configurationSource(corsConfigurationSource))
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.GET, "/catalog/docs").permitAll()
                        .pathMatchers(HttpMethod.GET, "/catalog/**").access(new OptionalAuthAuthorizationManager())
                        .pathMatchers(HttpMethod.POST, "/catalog/**").hasAuthority("EMPLOYEE")
                        .pathMatchers(HttpMethod.PUT, "/catalog/**").hasAuthority("EMPLOYEE")
                        .pathMatchers(HttpMethod.DELETE, "/catalog/**").hasAuthority("EMPLOYEE")
                        .pathMatchers(
                                "/oauth2/docs",
                                "/oauth2/login/**",
                                "/oauth2/registration/customer/**",
                                "/oauth2/users/recover-password").permitAll()
                        .pathMatchers(HttpMethod.GET, "/reviews/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/reviews/**").hasAuthority("CUSTOMER")
                        .pathMatchers(HttpMethod.PUT, "/reviews/**").hasAuthority("CUSTOMER")
                        .pathMatchers(HttpMethod.DELETE, "/reviews/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                        .pathMatchers(HttpMethod.GET, "/orders/docs").permitAll()
                        .pathMatchers(HttpMethod.POST, "/orders").access(postOrderEndpointAuthorizationManager)
                        .pathMatchers(HttpMethod.GET, "/orders/{id}").hasAnyAuthority("CUSTOMER", "EMPLOYEE")
                        .pathMatchers(HttpMethod.GET, "/orders/customer").hasAuthority("CUSTOMER")
                        .pathMatchers(HttpMethod.DELETE, "/orders/**").hasAuthority("CUSTOMER")
                        .pathMatchers("/orders/cart").hasAuthority("CUSTOMER")
                        .pathMatchers("/customers/wishlist/**").hasAuthority("CUSTOMER")
                        .pathMatchers("/customers/docs").permitAll()
                        .pathMatchers("/docs/**", "/swagger-ui", "/webjars/swagger-ui/**").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth -> oauth.authenticationManagerResolver(authenticationManagerResolver()))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

    @Bean
    public ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver() {
        var jwtAuthConverter = new JwtAuthenticationTokenConverter(authConfigurationProperties);
        Map<String, Mono<ReactiveAuthenticationManager>> jwtManagers = Stream.of(authConfigurationProperties.getIssuers())
                .map(OAuth2ConfigurationProperties.IssuerProperties::getUri)
                .collect(Collectors.toMap(iss -> iss, iss -> Mono.just(authenticationManager(iss, jwtAuthConverter))));
        return new JwtIssuerReactiveAuthenticationManagerResolver(issuerLocation -> jwtManagers.getOrDefault(issuerLocation, Mono.empty()));
    }

    private ReactiveAuthenticationManager authenticationManager(String issuer, JwtAuthenticationTokenConverter jwtAuthConverter) {
        ReactiveJwtDecoder decoder = ReactiveJwtDecoders.fromIssuerLocation(issuer);
        var authManager = new JwtReactiveAuthenticationManager(decoder);
        authManager.setJwtAuthenticationConverter(jwtAuthConverter);
        return authManager;
    }
}
