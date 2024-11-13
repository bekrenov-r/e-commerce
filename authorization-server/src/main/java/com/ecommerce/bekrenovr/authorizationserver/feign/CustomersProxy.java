package com.ecommerce.bekrenovr.authorizationserver.feign;

import com.ecommerce.bekrenovr.authorizationserver.registration.CustomerRequest;
import com.ecommerce.bekrenovr.authorizationserver.registration.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customers", path = "/customers")
public interface CustomersProxy {
    @PostMapping
    ResponseEntity<?> createCustomer(@RequestBody CustomerRequest request);

    @GetMapping
    ResponseEntity<CustomerResponse> getCustomerByEmail(@RequestParam String email);

    @PutMapping
    ResponseEntity<?> updateCustomer(@RequestBody CustomerRequest request);
}
