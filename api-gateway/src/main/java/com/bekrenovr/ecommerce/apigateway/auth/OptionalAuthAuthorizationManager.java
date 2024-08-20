package com.bekrenovr.ecommerce.apigateway.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OptionalAuthAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final GrantedAuthority requiredAuthorityIfAuthenticated;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .map(auth -> {
                    boolean granted = auth.getAuthorities().stream()
                            .anyMatch(requiredAuthorityIfAuthenticated::equals);
                    return new AuthorizationDecision(granted);
                })
                .defaultIfEmpty(new AuthorizationDecision(true));
    }
}
