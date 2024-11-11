package com.bekrenovr.ecommerce.reviews.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "catalog", path = "/catalog")
public interface CatalogProxy {
    @GetMapping("/items/{id}")
    ResponseEntity<?> getItemById(@PathVariable UUID id);
}
