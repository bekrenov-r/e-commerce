package com.ecommerce.bekrenovr.authorizationserver.registration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Validated
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/customer")
    public ResponseEntity<Void> registerCustomer(@RequestBody @Valid CustomerRegistrationRequest request) {
        registrationService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/customer/resend-email")
    public void resendActivationEmail(@RequestParam @NotBlank @Email String email) {
        registrationService.resendActivationEmail(email);
    }

    @PostMapping("/customer/activate-account")
    public ResponseEntity<?> activateAccount(@RequestParam @NotBlank String token) {
        return registrationService.activateAccount(token);
    }
}
