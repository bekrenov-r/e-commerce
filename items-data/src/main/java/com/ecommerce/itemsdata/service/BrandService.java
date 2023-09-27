package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.model.Brand;
import com.ecommerce.itemsdata.repository.BrandRepository;
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
