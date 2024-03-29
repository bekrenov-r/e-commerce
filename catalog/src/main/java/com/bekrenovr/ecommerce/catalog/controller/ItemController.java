package com.bekrenovr.ecommerce.catalog.controller;

import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptions;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemImageResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.model.enums.Gender;
import com.bekrenovr.ecommerce.catalog.service.ItemImageService;
import com.bekrenovr.ecommerce.catalog.service.ItemService;
import com.bekrenovr.ecommerce.catalog.service.sort.SortOption;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemImageService itemImageService;

    // temp
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable UUID id){
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @GetMapping("/gender/{gender}/category/{categoryId}")
    public ResponseEntity<Page<ItemResponse>> getAllItemsByGenderAndCategory(
            @PathVariable("gender") Gender gender,
            @PathVariable("categoryId") UUID categoryId,
            @RequestParam(name = "sort", required = false) SortOption sort,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @ModelAttribute @Valid FilterOptions filters
    ){
        return ResponseEntity
                .ok(itemService.getAllItemsByGenderAndCategory(gender, categoryId, sort, page, filters));
    }

    @GetMapping("/gender/{gender}/category/{categoryId}/subcategory/{subcategoryId}")
    public ResponseEntity<Page<ItemResponse>> getAllItemsByGenderCategoryAndSubcategory(
            @PathVariable("gender") Gender gender,
            @PathVariable("categoryId") UUID categoryId,
            @PathVariable("subcategoryId") UUID subcategoryId,
            @RequestParam(name = "sort", required = false) SortOption sort,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @ModelAttribute FilterOptions filters
    ) {
        return ResponseEntity
                .ok(itemService.getAllItemsByGenderCategoryAndSubcategory(
                        gender, categoryId, subcategoryId, sort, page, filters
                ));
    }

    @GetMapping("/{itemId}/images")
    public ResponseEntity<List<ItemImageResponse>> getAllImagesForItem(@PathVariable UUID itemId){
        return ResponseEntity.ok(itemImageService.getAllImagesForItem(itemId));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Item item) throws Exception {
        return itemService.createItem(item);
    }

    // method for dev purpose
    @GetMapping("/createSampleItems/{quantity}")
    public void createSampleItems(@PathVariable Integer quantity){
        itemService.createSampleItems(quantity);
    }

}
