package com.bekrenovr.ecommerce.orders.proxy;

import com.bekrenovr.ecommerce.orders.dto.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "catalog")
public interface ItemProxy {
    @GetMapping("/catalog/items/{id}")
    ResponseEntity<ItemResponse> getById(@PathVariable UUID id);

    @GetMapping("/catalog/items/list/{ids}")
    ResponseEntity<List<ItemResponse>> getItemsByIds(@PathVariable List<UUID> ids);

}
