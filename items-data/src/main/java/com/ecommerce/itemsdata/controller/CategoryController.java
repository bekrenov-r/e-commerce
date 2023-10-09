package com.ecommerce.itemsdata.controller;

import com.ecommerce.itemsdata.dto.response.CategoryResponse;
import com.ecommerce.itemsdata.dto.response.SubcategoryResponse;
import com.ecommerce.itemsdata.model.Gender;
import com.ecommerce.itemsdata.service.CategoryService;
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
