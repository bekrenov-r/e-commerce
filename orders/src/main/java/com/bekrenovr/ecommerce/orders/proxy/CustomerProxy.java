package com.bekrenovr.ecommerce.orders.proxy;

import com.bekrenovr.ecommerce.orders.dto.request.CustomerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "customers", path = "/customers")
public interface CustomerProxy {
    @PostMapping
    ResponseEntity<Void> createCustomer(@RequestBody CustomerRequest request);
}
