package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import com.bekrenovr.ecommerce.users.service.RegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2/registration")
@RequiredArgsConstructor
@Validated
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/customer")
    public ResponseEntity<Void> registerCustomerBasic(@RequestBody @Valid CustomerRegistrationRequest request) {
        registrationService.registerCustomerBasic(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/customer/resend-email")
    public void resendActivationEmail(@RequestParam @NotBlank @Email String email) {
        registrationService.resendActivationEmail(email);
    }

    @PostMapping("/customer/activate-account")
    public ResponseEntity<String> activateAccount(@RequestParam @NotBlank String token) {
        return registrationService.activateAccount(token);
    }
}
