package com.bekrenovr.ecommerce.apigateway.auth;

import com.jayway.jsonpath.JsonPath;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> {
    private static final String AUTHORITIES_JSON_PATH = "$.realm_access.roles";
    private static final String USERNAME_JSON_PATH = "$.preferred_username";

    @Override
    @SuppressWarnings("unchecked")
    public Mono<? extends AbstractAuthenticationToken> convert(Jwt jwt) {
        Collection<? extends GrantedAuthority> authorities = ((List<String>)JsonPath.read(jwt.getClaims(), AUTHORITIES_JSON_PATH))
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        String username = JsonPath.read(jwt.getClaims(), USERNAME_JSON_PATH);
        return Mono.just(new JwtAuthenticationToken(jwt, authorities, username));
    }
}
