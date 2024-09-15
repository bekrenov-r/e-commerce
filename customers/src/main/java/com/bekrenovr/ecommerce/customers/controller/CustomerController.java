package com.bekrenovr.ecommerce.customers.controller;

import com.bekrenovr.ecommerce.customers.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.customers.dto.response.CustomerResponse;
import com.bekrenovr.ecommerce.customers.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PostMapping
    public void createCustomer(@Valid @RequestBody CustomerRequest request){
        customerService.createCustomer(request, false);
    }

    @PutMapping
    public void update(@Valid @RequestBody CustomerRequest request){
        customerService.updateCustomer(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return customerService.delete(id);
    }
}
