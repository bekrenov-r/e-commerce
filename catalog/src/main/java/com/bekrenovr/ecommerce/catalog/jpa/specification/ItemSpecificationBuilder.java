package com.bekrenovr.ecommerce.catalog.jpa.specification;

import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptions;
import com.bekrenovr.ecommerce.catalog.jpa.repository.BrandRepository;
import com.bekrenovr.ecommerce.catalog.jpa.repository.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.model.entity.Brand;
import com.bekrenovr.ecommerce.catalog.model.entity.Category;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.model.entity.Subcategory;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.CATEGORY_NOT_FOUND;
import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.SUBCATEGORY_NOT_FOUND;
import static com.bekrenovr.ecommerce.catalog.jpa.specification.ItemSpecification.*;

@Component
@RequiredArgsConstructor
public class ItemSpecificationBuilder {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public Specification<Item> buildFromFilterOptions(FilterOptions filterOptions){
        Collection<Specification<Item>> specifications = new HashSet<>();

        var specFromCategoryAndSubcategory = resolveFromCategoryAndSubcategory(filterOptions.categoryId(), filterOptions.subcategoryId());
        if(specFromCategoryAndSubcategory != null){
            specifications.add(specFromCategoryAndSubcategory);
        }
        if(filterOptions.brandsIds() != null){
            Collection<Brand> brands = brandRepository.findAllById(filterOptions.brandsIds());
            specifications.add(hasBrandIn(brands));
        }
        if(filterOptions.gender() != null){
            specifications.add(hasGender(filterOptions.gender()));
        }
        if(filterOptions.priceRange() != null){
            specifications.add(hasPriceWithinRange(filterOptions.priceRange()));
        }
        if(filterOptions.sizes() != null){
            specifications.add(hasSizeIn(filterOptions.sizes()));
        }
        if(filterOptions.colors() != null){
            specifications.add(hasColorIn(filterOptions.colors()));
        }
        if(filterOptions.materials() != null){
            specifications.add(hasMaterialIn(filterOptions.materials()));
        }
        if(filterOptions.season() != null){
            specifications.add(hasSeason(filterOptions.season()));
        }
        if(filterOptions.rating() != null){
            specifications.add(hasRatingGreaterThan(filterOptions.rating()));
        }
        if(filterOptions.searchPattern() != null){
            specifications.add(matchesSearchPattern(filterOptions.searchPattern()));
        }
        return Specification.allOf(specifications);
    }

    private Specification<Item> resolveFromCategoryAndSubcategory(UUID categoryId, UUID subcategoryId){
        if(categoryId != null){
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EcommerceApplicationException(CATEGORY_NOT_FOUND, categoryId));
            if(subcategoryId != null){
                Subcategory subcategory = category.getSubcategories().stream()
                        .filter(sub -> sub.getId().equals(subcategoryId))
                        .findFirst()
                        .orElseThrow(() -> new EcommerceApplicationException(SUBCATEGORY_NOT_FOUND, categoryId, subcategoryId));
                return hasCategoryAndSubcategory(category, subcategory);
            } else {
                return hasCategory(category);
            }
        }
        return null;
    }
}
