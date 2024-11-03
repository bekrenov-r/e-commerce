package com.ecommerce.bekrenovr.authorizationserver.service;

import com.ecommerce.bekrenovr.authorizationserver.dto.request.CustomerRegistrationRequest;
import com.ecommerce.bekrenovr.authorizationserver.dto.response.CustomerResponse;
import com.ecommerce.bekrenovr.authorizationserver.proxy.CustomerServiceProxy;
import com.ecommerce.bekrenovr.authorizationserver.proxy.KeycloakProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final KeycloakProxy keycloakProxy;
    private final CustomerServiceProxy customerServiceProxy;
    private final MailService mailService;

    public void registerCustomer(CustomerRegistrationRequest request) {
        ResponseEntity<String> response = keycloakProxy.createKeycloakUser(
                request.getEmail(), request.getPassword(), "CUSTOMER", request.getFirstName()
        );
        request.setRegistered(true);
        customerServiceProxy.createCustomer(request);
        mailService.sendCustomerAccountActivationEmail(request, response.getBody());
    }

    public void resendActivationEmail(String email) {
        ResponseEntity<CustomerResponse> response = customerServiceProxy.getCustomerByEmail(email);
        String activationToken = keycloakProxy.getActivationTokenForUser(email).getBody();
        mailService.sendCustomerAccountActivationEmail(response.getBody(), activationToken);
    }

    public ResponseEntity<?> activateAccount(String token) {
        return keycloakProxy.enableUser(token);
    }
}
