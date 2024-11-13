package com.bekrenovr.ecommerce.customers.customer;

import com.bekrenovr.ecommerce.customers.customer.dto.CustomerRequest;
import com.bekrenovr.ecommerce.customers.customer.dto.CustomerResponse;
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

    @GetMapping
    public ResponseEntity<CustomerResponse> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.getByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CustomerRequest request){
        HttpStatus status = customerService.create(request);
        return ResponseEntity.status(status).build();
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> update(@Valid @RequestBody CustomerRequest request){
        return ResponseEntity.ok(customerService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return customerService.delete(id);
    }
}
