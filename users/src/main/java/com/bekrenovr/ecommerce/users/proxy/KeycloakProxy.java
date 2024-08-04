package com.bekrenovr.ecommerce.users.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "keycloak")
public interface KeycloakProxy {
    @PostMapping("/users")
    ResponseEntity<String> createKeycloakUser(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              @RequestParam("role") String role);
}
