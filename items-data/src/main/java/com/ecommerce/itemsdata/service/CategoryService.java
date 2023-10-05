package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.dto.mapper.CategoryMapper;
import com.ecommerce.itemsdata.dto.mapper.SubcategoryMapper;
import com.ecommerce.itemsdata.dto.response.CategoryResponse;
import com.ecommerce.itemsdata.dto.response.SubcategoryResponse;
import com.ecommerce.itemsdata.exception.ItemApplicationException;
import com.ecommerce.itemsdata.model.Category;
import com.ecommerce.itemsdata.model.Gender;
import com.ecommerce.itemsdata.repository.CategoryRepository;
import com.ecommerce.itemsdata.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.itemsdata.exception.ItemApplicationExceptionReason.CATEGORY_NOT_FOUND;

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

    public List<SubcategoryResponse> getAllSubcategoriesInCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(CATEGORY_NOT_FOUND, categoryId));
        return subcategoryRepository.findAllByCategory(category)
                .stream()
                .map(subcategoryMapper::subcategoryToResponse)
                .toList();
    }
}
