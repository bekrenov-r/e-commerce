package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.users.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getWishListItemsForCustomer(){
        return ResponseEntity.ok(wishListService.getWishListItemsForCustomer());
    }

    @PostMapping
    public ResponseEntity<Void> addItemToWishList(@RequestParam(name = "id") UUID itemId) {
        wishListService.addItemToWishList(itemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeItemFromWishList(@RequestParam(name = "id") UUID itemId) {
        wishListService.removeItemFromWishList(itemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
