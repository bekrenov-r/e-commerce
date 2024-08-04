package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import com.bekrenovr.ecommerce.users.proxy.KeycloakProxy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final CustomerService customerService;
    private final KeycloakProxy keycloakProxy;
    private final MailService mailService;

    @Transactional
    public void registerCustomer(CustomerRegistrationRequest request) {
        customerService.createCustomer(request, true);
        ResponseEntity<String> response = keycloakProxy.createKeycloakUser(
                request.getEmail(), request.getPassword(), "CUSTOMER"
        );
        mailService.sendCustomerAccountActivationEmail(request, response.getBody());
    }
}
