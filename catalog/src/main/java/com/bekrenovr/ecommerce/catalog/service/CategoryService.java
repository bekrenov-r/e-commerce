package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.CategoryMapper;
import com.bekrenovr.ecommerce.catalog.dto.mapper.SubcategoryMapper;
import com.bekrenovr.ecommerce.catalog.dto.response.CategoryResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.SubcategoryResponse;
import com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationException;
import com.bekrenovr.ecommerce.catalog.jpa.repository.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.jpa.repository.SubcategoryRepository;
import com.bekrenovr.ecommerce.catalog.model.entity.Category;
import com.bekrenovr.ecommerce.catalog.model.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryMapper categoryMapper;
    private final SubcategoryMapper subcategoryMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::categoryToResponse)
                .toList();
    }

    public List<SubcategoryResponse> getAllSubcategoriesInCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CatalogApplicationException(CATEGORY_NOT_FOUND, categoryId));
        return subcategoryRepository.findAllByCategory(category)
                .stream()
                .map(subcategoryMapper::subcategoryToResponse)
                .toList();
    }

    public Map<UUID, byte[]> getAllCategoryImages(Gender gender) {
        return categoryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Category::getId,
                        c -> categoryMapper.mapCategoryImage(c.getImageName(), gender)
                ));
    }
}
