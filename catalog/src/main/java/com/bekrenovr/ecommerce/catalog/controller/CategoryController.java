package com.bekrenovr.ecommerce.catalog.controller;

import com.bekrenovr.ecommerce.catalog.dto.response.CategoryResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.SubcategoryResponse;
import com.bekrenovr.ecommerce.catalog.model.Gender;
import com.bekrenovr.ecommerce.catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@RequestParam Gender gender){
        return ResponseEntity.ok(categoryService.getAllCategories(gender));
    }

    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubcategoryResponse>> getAllSubcategoriesInCategory(@PathVariable String categoryId){
        return ResponseEntity.ok(categoryService.getAllSubcategoriesInCategory(categoryId));
    }
}
