package com.bekrenovr.ecommerce.users.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("catalog")
public interface CatalogProxy {
    @GetMapping("/catalog/items/{id}")
    ResponseEntity<?> getItemById(@PathVariable UUID id);

    @GetMapping("/catalog/items/list")
    ResponseEntity<?> getItemsByIds(@RequestParam List<UUID> ids);
}
