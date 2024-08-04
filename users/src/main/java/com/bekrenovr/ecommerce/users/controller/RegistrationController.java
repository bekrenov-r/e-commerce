package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import com.bekrenovr.ecommerce.users.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/customer")
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        registrationService.registerCustomer(request);
    }
}
