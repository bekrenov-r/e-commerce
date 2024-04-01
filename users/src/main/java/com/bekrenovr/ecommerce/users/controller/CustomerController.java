package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.users.dto.CustomerDTO;
import com.bekrenovr.ecommerce.users.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.users.entity.Customer;
import com.bekrenovr.ecommerce.users.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id){
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody CustomerRequest request,
            @RequestParam("withUser") boolean withUser
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.createCustomer(request, withUser));
    }

    @PutMapping
    public ResponseEntity<Customer> update(@Valid @RequestBody Customer customer){
        return customerService.update(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return customerService.delete(id);
    }
}
