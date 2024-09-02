package com.bekrenovr.ecommerce.apigateway.auth;

import com.bekrenovr.ecommerce.apigateway.config.OAuth2ConfigurationProperties;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenConverter implements Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> {
    private final OAuth2ConfigurationProperties authConfigurationProperties;

    @Override
    public Mono<? extends AbstractAuthenticationToken> convert(Jwt jwt) {
        URI iss = URI.create(jwt.getClaim(JwtClaimNames.ISS));
        OAuth2ConfigurationProperties.IssuerProperties issuerProperties = authConfigurationProperties.get(iss);
        Collection<? extends GrantedAuthority> authorities = new JwtGrantedAuthoritiesConverter(issuerProperties).convert(jwt);
        String username = JsonPath.read(jwt.getClaims(), issuerProperties.getUsernameJsonPath());
        return Mono.just(new JwtAuthenticationToken(jwt, authorities, username));
    }
}
