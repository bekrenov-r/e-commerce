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

    @PostMapping("/customer/basic")
    public ResponseEntity<Void> registerCustomerBasic(@RequestBody CustomerRegistrationRequest request) {
        registrationService.registerCustomerBasic(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/customer/resend-email")
    public ResponseEntity<Void> resendActivationEmail(@RequestParam String email) {
        registrationService.resendActivationEmail(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/customer/google")
    public ResponseEntity<String> registerCustomerGoogle(
            @RequestParam("code") String code,
            @RequestParam("scope") String scope,
            @RequestParam("authuser") String authUser,
            @RequestParam("prompt") String prompt
    ) {
        return ResponseEntity.ok(registrationService.registerCustomerGoogle(code));
    }
}
