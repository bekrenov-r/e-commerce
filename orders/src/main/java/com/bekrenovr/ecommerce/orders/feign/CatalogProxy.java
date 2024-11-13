package com.bekrenovr.ecommerce.orders.feign;

import com.bekrenovr.ecommerce.orders.order.dto.CatalogItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "catalog")
public interface CatalogProxy {
    @GetMapping("/catalog/items/{id}")
    ResponseEntity<CatalogItem> getItemById(@PathVariable UUID id);

    @GetMapping("/catalog/items/list")
    ResponseEntity<List<CatalogItem>> getItemsByIds(@RequestParam List<UUID> ids);

}
