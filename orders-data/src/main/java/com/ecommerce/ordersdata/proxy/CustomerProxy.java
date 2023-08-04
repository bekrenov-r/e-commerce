package com.ecommerce.ordersdata.proxy;

import com.ecommerce.ordersdata.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-data")
public interface CustomerProxy {
    @GetMapping("/customers/{id}")
    ResponseEntity<CustomerDTO> getById(@PathVariable("id") Integer id);

}
