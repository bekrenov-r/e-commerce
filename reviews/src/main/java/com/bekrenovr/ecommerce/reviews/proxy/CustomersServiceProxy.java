package com.bekrenovr.ecommerce.reviews.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "customers", path = "/customers")
public interface CustomersServiceProxy {
    @GetMapping
    ResponseEntity<Map<String, String>> getCustomerByEmail(@RequestParam String email);
}
