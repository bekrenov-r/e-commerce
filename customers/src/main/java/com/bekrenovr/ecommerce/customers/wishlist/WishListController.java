package com.bekrenovr.ecommerce.customers.wishlist;

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
    public ResponseEntity<?> getForCustomer(){
        return wishListService.getForCustomer();
    }

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestParam(name = "id") UUID itemId) {
        wishListService.addItem(itemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public void removeItem(@RequestParam(name = "id") UUID itemId) {
        wishListService.removeItem(itemId);
    }

    @DeleteMapping("/clear")
    public void clear() {
        wishListService.clear();
    }
}
