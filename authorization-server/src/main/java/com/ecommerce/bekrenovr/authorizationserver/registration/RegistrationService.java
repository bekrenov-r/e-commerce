package com.ecommerce.bekrenovr.authorizationserver.registration;

import com.ecommerce.bekrenovr.authorizationserver.feign.CustomersProxy;
import com.ecommerce.bekrenovr.authorizationserver.feign.KeycloakProxy;
import com.ecommerce.bekrenovr.authorizationserver.support.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final KeycloakProxy keycloakProxy;
    private final CustomersProxy customersProxy;
    private final MailService mailService;

    public void registerCustomer(CustomerRegistrationRequest request) {
        ResponseEntity<String> response = keycloakProxy.createKeycloakUser(
                request.getEmail(), request.getPassword(), "CUSTOMER", request.getFirstName()
        );
        request.setRegistered(true);
        customersProxy.createCustomer(request);
        mailService.sendCustomerAccountActivationEmail(request, response.getBody());
    }

    public void resendActivationEmail(String email) {
        ResponseEntity<CustomerResponse> response = customersProxy.getCustomerByEmail(email);
        String activationToken = keycloakProxy.getActivationTokenForUser(email).getBody();
        mailService.sendCustomerAccountActivationEmail(response.getBody(), activationToken);
    }

    public ResponseEntity<?> activateAccount(String token) {
        return keycloakProxy.enableUser(token);
    }
}
