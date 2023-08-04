package com.ecommerce.itemsdata.controller;

import com.ecommerce.itemsdata.dto.response.ItemDetailedResponse;
import com.ecommerce.itemsdata.dto.response.ItemResponse;
import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailedResponse> getItemById(@PathVariable Long id){
        // todo: to be implemented
        return null;
    }

    @GetMapping("/gender/{gender}/category/{categoryId}")
    public ResponseEntity<List<ItemResponse>> getAllItemsByGenderAndCategory(
            @PathVariable("gender") Gender gender,
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "priceRange", required = false) String priceRange,
            @RequestParam(name = "size", required = false) String sizes,
            @RequestParam(name = "color", required = false) String colors,
            @RequestParam(name = "brand", required = false) String brands,
            @RequestParam(name = "season", required = false) Season season,
            @RequestParam(name = "materials", required = false) String materials,
            @RequestParam(name = "minRating", required = false) Double rating
    ){
        return ResponseEntity.ok(
                itemService.getAllItemsByGenderAndCategoryWithFilters(gender, categoryId, sort, priceRange, sizes, colors, brands, season, materials, rating)
        );
    }

    @GetMapping("/gender/{gender}/category/{category}/{subcategory}")
    public ResponseEntity<List<ItemResponse>> getAllItemsByGenderCategoryAndSubcategory(
            @PathVariable("gender") Gender gender,
            @PathVariable("category") String category,
            @PathVariable("subcategory") String subcategory,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "priceRange", required = false) String priceRange,
            @RequestParam(name = "size", required = false) String sizes,
            @RequestParam(name = "color", required = false) String colors,
            @RequestParam(name = "brand", required = false) String brands,
            @RequestParam(name = "season", required = false) Season season,
            @RequestParam(name = "materials", required = false) String materials,
            @RequestParam(name = "minRating", required = false) Double rating
    ){
        return null;
    }

    @GetMapping("/age-group/{age-group}/gender/{gender}/category/{category}")
    public ResponseEntity<List<ItemResponse>> getAllItemsByAgeGenderAndCategory(
            @PathVariable("age-group") AgeGroup ageGroup,
            @PathVariable("gender") Gender gender,
            @PathVariable("category") String category,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "priceRange", required = false) String priceRange,
            @RequestParam(name = "size", required = false) String sizes,
            @RequestParam(name = "color", required = false) String colors,
            @RequestParam(name = "brand", required = false) String brands,
            @RequestParam(name = "season", required = false) Season season,
            @RequestParam(name = "materials", required = false) String materials,
            @RequestParam(name = "minRating", required = false) Double rating
    ){
        return null;
    }

    @GetMapping("/age-group/{age-group}/gender/{gender}/category/{category}/{subcategory}")
    public ResponseEntity<List<ItemResponse>> getAllItemsByAgeGenderCategoryAndSubcategory(
            @PathVariable("age-group") AgeGroup ageGroup,
            @PathVariable("gender") Gender gender,
            @PathVariable("category") String category,
            @PathVariable("subcategory") String subcategory,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "priceRange", required = false) String priceRange,
            @RequestParam(name = "size", required = false) String sizes,
            @RequestParam(name = "color", required = false) String colors,
            @RequestParam(name = "brand", required = false) String brands,
            @RequestParam(name = "season", required = false) Season season,
            @RequestParam(name = "materials", required = false) String materials,
            @RequestParam(name = "minRating", required = false) Double rating
    ){
        return null;
    }

    @GetMapping("/by-season/{season}")
    public ResponseEntity<List<ItemResponse>> getAllItemsBySeason(
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
    public ResponseEntity<List<ItemResponse>> getAllItemsByCollection(
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
    @GetMapping("/createSampleItems")
    public void createSampleItems(){
        itemService.createSampleItems(100);
    }

}
