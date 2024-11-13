package com.ecommerce.bekrenovr.authorizationserver.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "keycloak", path = "/keycloak/realms/e-commerce")
public interface KeycloakProxy {
    @PostMapping("/users")
    ResponseEntity<String> createKeycloakUser(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              @RequestParam("role") String role,
                                              @RequestParam("firstName") String firstName);

    @PostMapping(
            value = "/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<String> getAccessToken(@RequestBody Map<String, ?> form);

    @GetMapping("/users/activation-token")
    ResponseEntity<String> getActivationTokenForUser(@RequestParam("username") String username);

    @PostMapping("/users/enable")
    ResponseEntity<?> enableUser(@RequestParam String token);

    @PostMapping("/users/recover-password")
    ResponseEntity<?> recoverPassword(@RequestParam String token, @RequestParam("password") String newPassword);

    @PostMapping("/users/recover-password/token")
    ResponseEntity<?> createPasswordRecoveryToken(@RequestParam String username, @RequestParam String token);
}
