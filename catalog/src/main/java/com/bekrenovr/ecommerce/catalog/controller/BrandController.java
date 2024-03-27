package com.bekrenovr.ecommerce.catalog.controller;

import com.bekrenovr.ecommerce.catalog.model.entity.Brand;
import com.bekrenovr.ecommerce.catalog.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }
}
