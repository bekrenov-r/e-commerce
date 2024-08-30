package com.bekrenovr.ecommerce.orders.controller;

import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.service.CartService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
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

    @PutMapping
    public void updateCartItem(
            @RequestParam("entryId") UUID itemEntryId,
            @RequestParam("quantity") @Positive int quantity,
            @RequestParam("size") String size
    ) {
        cartService.updateCartItem(itemEntryId, quantity, size);
    }

    @DeleteMapping
    public void removeItemFromCart(@RequestParam("entryId") UUID itemEntryId){
        cartService.removeItemFromCart(itemEntryId);
    }

    @DeleteMapping("/clear")
    public void clearCart(){
        cartService.clearCart();
    }
}
