package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import com.bekrenovr.ecommerce.users.model.entity.Customer;
import com.bekrenovr.ecommerce.users.proxy.KeycloakProxy;
import com.bekrenovr.ecommerce.users.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
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

    public void resendActivationEmail(String email) {
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        String activationToken = keycloakProxy.getActivationTokenForUser(customer.getEmail()).getBody();
        mailService.sendCustomerAccountActivationEmail(customer, activationToken);
    }
}
