package com.bekrenovr.ecommerce.orders.controller;

import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<ItemEntryResponse>> getCartItems(){
        return ResponseEntity.ok(cartService.getCartItems());
    }

    @PostMapping
    public ResponseEntity<Void> addItemToCart(@RequestBody ItemEntryRequest request){
        cartService.addItemToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public void removeItemFromCart(@RequestParam("id") UUID itemEntryId){
        cartService.removeItemFromCart(itemEntryId);
    }
}
