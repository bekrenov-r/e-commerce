package com.bekrenovr.ecommerce.users.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "keycloak")
public interface KeycloakProxy {
    @PostMapping("/keycloak/realms/e-commerce/users")
    ResponseEntity<String> createKeycloakUser(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              @RequestParam("role") String role,
                                              @RequestParam("firstName") String firstName);

    @GetMapping("/keycloak/realms/e-commerce/users/activation-token")
    ResponseEntity<String> getActivationTokenForUser(@RequestParam("username") String username);
}
