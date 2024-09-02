package com.bekrenovr.ecommerce.apigateway.auth;


import com.bekrenovr.ecommerce.apigateway.config.OAuth2ConfigurationProperties;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<? extends GrantedAuthority>> {
    private final OAuth2ConfigurationProperties.IssuerProperties issuerProperties;

    @Override
    public Collection<? extends GrantedAuthority> convert(Jwt jwt) {
        return extractRoles(jwt).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private Stream<String> extractRoles(Jwt jwt) {
        if(issuerProperties.hasDefaultRoles()) {
            return Stream.of(issuerProperties.getDefaultRoles());
        } else {
            return ((List<String>) JsonPath.read(jwt.getClaims(), issuerProperties.getRolesJsonPath())).stream();
        }
    }
}
