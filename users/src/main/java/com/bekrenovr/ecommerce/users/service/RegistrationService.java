package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final CustomerService customerService;

    @Transactional
    public void registerCustomer(CustomerRegistrationRequest request) {
        customerService.createCustomer(request, true);
        addKeycloakUser(request.getEmail(), request.getPassword(), "CUSTOMER");
    }

    private void addKeycloakUser(String email, String password, String role) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = Map.of("username", email, "password", password, "role", role);
        restTemplate.postForEntity(
                "http://localhost:8400/users?username={username}&password={password}&role={role}",
                null, Void.class, params);
    }
}
