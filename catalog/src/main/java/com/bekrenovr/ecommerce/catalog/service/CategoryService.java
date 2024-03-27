package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.CategoryMapper;
import com.bekrenovr.ecommerce.catalog.dto.mapper.SubcategoryMapper;
import com.bekrenovr.ecommerce.catalog.dto.response.CategoryResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.SubcategoryResponse;
import com.bekrenovr.ecommerce.catalog.exception.ItemApplicationException;
import com.bekrenovr.ecommerce.catalog.model.entity.Category;
import com.bekrenovr.ecommerce.catalog.model.enums.Gender;
import com.bekrenovr.ecommerce.catalog.repository.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.ItemApplicationExceptionReason.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryMapper categoryMapper;
    private final SubcategoryMapper subcategoryMapper;

    public List<CategoryResponse> getAllCategories(Gender gender) {
        return categoryRepository.findAll().stream()
                .map(category -> categoryMapper.categoryToResponse(category, gender))
                .toList();
    }

    public List<SubcategoryResponse> getAllSubcategoriesInCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(CATEGORY_NOT_FOUND, categoryId));
        return subcategoryRepository.findAllByCategory(category)
                .stream()
                .map(subcategoryMapper::subcategoryToResponse)
                .toList();
    }
}
