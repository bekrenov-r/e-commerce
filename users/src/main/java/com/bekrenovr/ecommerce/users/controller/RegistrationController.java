package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import com.bekrenovr.ecommerce.users.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/customer")
    public ResponseEntity<Void> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        registrationService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/customer/resend-email")
    public ResponseEntity<Void> resendActivationEmail(@RequestParam String email) {
        registrationService.resendActivationEmail(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
