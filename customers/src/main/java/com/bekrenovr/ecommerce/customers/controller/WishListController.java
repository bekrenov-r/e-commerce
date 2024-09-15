package com.bekrenovr.ecommerce.customers.controller;

import com.bekrenovr.ecommerce.customers.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @GetMapping
    public ResponseEntity<?> getWishListItemsForCustomer(){
        return wishListService.getWishListItemsForCustomer();
    }

    @PostMapping
    public ResponseEntity<Void> addItemToWishList(@RequestParam(name = "id") UUID itemId) {
        wishListService.addItemToWishList(itemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public void removeItemFromWishList(@RequestParam(name = "id") UUID itemId) {
        wishListService.removeItemFromWishList(itemId);
    }

    @DeleteMapping("/clear")
    public void clearWishList() {
        wishListService.clearWishList();
    }
}
