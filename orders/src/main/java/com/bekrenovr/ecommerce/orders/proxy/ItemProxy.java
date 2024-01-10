package com.bekrenovr.ecommerce.orders.proxy;

import com.bekrenovr.ecommerce.orders.dto.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "items-data")
public interface ItemProxy {
    @GetMapping("/items/{id}")
    ResponseEntity<ItemResponse> getById(@PathVariable("id") Integer id);

    @GetMapping("/items/list/{ids}")
    ResponseEntity<List<ItemResponse>> getItemsByIds(@PathVariable("ids") List<Integer> ids);

}
