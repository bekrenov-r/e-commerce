package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.jpa.repository.BrandRepository;
import com.bekrenovr.ecommerce.catalog.model.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
