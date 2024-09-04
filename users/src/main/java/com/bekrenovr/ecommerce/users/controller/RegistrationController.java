package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import com.bekrenovr.ecommerce.users.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/customer")
    public ResponseEntity<Void> registerCustomerBasic(@RequestBody CustomerRegistrationRequest request) {
        registrationService.registerCustomerBasic(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/customer/resend-email")
    public void resendActivationEmail(@RequestParam String email) {
        registrationService.resendActivationEmail(email);
    }

    @PostMapping("/customer/activate-account")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        return registrationService.activateAccount(token);
    }
}
