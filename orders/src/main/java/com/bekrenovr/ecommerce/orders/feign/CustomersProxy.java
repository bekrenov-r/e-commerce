package com.bekrenovr.ecommerce.orders.feign;

import com.bekrenovr.ecommerce.orders.order.dto.CustomerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "customers", path = "/customers")
public interface CustomersProxy {
    @PostMapping
    ResponseEntity<Void> createCustomer(@RequestBody CustomerRequest request);
}
