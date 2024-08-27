package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.users.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getWishListItemsForCustomer(){
        return ResponseEntity.ok(wishListService.getWishListItemsForCustomer());
    }
}
