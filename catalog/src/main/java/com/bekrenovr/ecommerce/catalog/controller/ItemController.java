package com.bekrenovr.ecommerce.catalog.controller;

import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptionsModel;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.model.Gender;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.model.Season;
import com.bekrenovr.ecommerce.catalog.service.ItemService;
import com.bekrenovr.ecommerce.catalog.service.sort.SortOption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

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
            @ModelAttribute FilterOptionsModel filters
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
            @ModelAttribute FilterOptionsModel filters
    ) {
        return ResponseEntity
                .ok(itemService.getAllItemsByGenderCategoryAndSubcategory(
                        gender, categoryId, subcategoryId, sort, page, filters
                ));
    }

    @GetMapping("/by-season/{season}")
    public ResponseEntity<Page<ItemResponse>> getAllItemsBySeason(
            @PathVariable Season season,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "priceRange", required = false) String priceRange,
            @RequestParam(name = "size", required = false) String sizes,
            @RequestParam(name = "color", required = false) String colors,
            @RequestParam(name = "brand", required = false) String brands,
            @RequestParam(name = "materials", required = false) String materials,
            @RequestParam(name = "minRating", required = false) Double rating
    ){
        return null;
    }

    @GetMapping("/by-collection/{collection}")
    public ResponseEntity<Page<ItemResponse>> getAllItemsByCollection(
            @PathVariable String collection,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "priceRange", required = false) String priceRange,
            @RequestParam(name = "size", required = false) String sizes,
            @RequestParam(name = "color", required = false) String colors,
            @RequestParam(name = "brand", required = false) String brands,
            @RequestParam(name = "materials", required = false) String materials,
            @RequestParam(name = "minRating", required = false) Double rating
    ){
        return null;
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
