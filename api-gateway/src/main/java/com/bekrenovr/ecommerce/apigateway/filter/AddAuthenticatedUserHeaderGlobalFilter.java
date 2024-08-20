package com.bekrenovr.ecommerce.apigateway.filter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AddAuthenticatedUserHeaderGlobalFilter implements GlobalFilter {
    private static final String AUTHENTICATED_USER = "Authenticated-User";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    if (authentication == null || !authentication.isAuthenticated()) {
                        return chain.filter(exchange);
                    }
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header(AUTHENTICATED_USER, convertAuthToJson(authentication))
                            .build();
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                }).then(chain.filter(exchange));
    }

    private String convertAuthToJson(Authentication authentication){
        Collection<String> authoritiesStr = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        JSONObject json = new JSONObject();
        json.put("username", authentication.getName());
        json.put("authorities", new JSONArray(authoritiesStr));
        return json.toString();
    }
}
