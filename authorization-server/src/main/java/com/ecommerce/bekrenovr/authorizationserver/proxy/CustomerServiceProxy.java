package com.ecommerce.bekrenovr.authorizationserver.proxy;

import com.ecommerce.bekrenovr.authorizationserver.dto.request.CustomerRequest;
import com.ecommerce.bekrenovr.authorizationserver.dto.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customers", path = "/customers")
public interface CustomerServiceProxy {
    @PostMapping
    ResponseEntity<?> createCustomer(@RequestBody CustomerRequest request);

    @GetMapping
    ResponseEntity<CustomerResponse> getCustomerByEmail(@RequestParam String email);

    @PutMapping
    ResponseEntity<?> updateCustomer(@RequestBody CustomerRequest request);
}
