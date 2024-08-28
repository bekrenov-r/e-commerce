package com.bekrenovr.ecommerce.orders.controller;

import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<ItemEntryResponse>> getCartItems(){
        return ResponseEntity.ok(cartService.getCartItems());
    }
}
