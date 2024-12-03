package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.brand.Brand;
import com.bekrenovr.ecommerce.catalog.brand.BrandRepository;
import com.bekrenovr.ecommerce.catalog.category.Category;
import com.bekrenovr.ecommerce.catalog.category.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.category.subcategory.Subcategory;
import com.bekrenovr.ecommerce.catalog.item.filters.FilterOptions;
import com.bekrenovr.ecommerce.catalog.util.convert.StringToDoubleRangeConverter;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.DoubleRange;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.CATEGORY_NOT_FOUND;
import static com.bekrenovr.ecommerce.catalog.exception.CatalogApplicationExceptionReason.SUBCATEGORY_NOT_FOUND;
import static com.bekrenovr.ecommerce.catalog.item.ItemSpecification.*;

@Component
@RequiredArgsConstructor
public class ItemSpecificationBuilder {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public Specification<Item> buildFromFilterOptions(FilterOptions filterOptions){
        Collection<Specification<Item>> specifications = new HashSet<>();

        var specFromCategoryAndSubcategory = resolveFromCategoryAndSubcategory(filterOptions.getCategoryId(), filterOptions.getSubcategoryId());
        if(specFromCategoryAndSubcategory != null){
            specifications.add(specFromCategoryAndSubcategory);
        }
        if(filterOptions.getBrandsIds() != null){
            Collection<Brand> brands = brandRepository.findAllById(filterOptions.getBrandsIds());
            specifications.add(hasBrandIn(brands));
        }
        if(filterOptions.getGender() != null){
            specifications.add(hasGender(filterOptions.getGender()));
        }
        if(filterOptions.getPriceRange() != null){
            DoubleRange priceRange = new StringToDoubleRangeConverter().convert(filterOptions.getPriceRange());
            specifications.add(hasPriceWithinRange(priceRange));
        }
        if(filterOptions.getSizes() != null){
            specifications.add(hasSizeIn(filterOptions.getSizes()));
        }
        if(filterOptions.getColors() != null){
            specifications.add(hasColorIn(filterOptions.getColors()));
        }
        if(filterOptions.getMaterials() != null){
            specifications.add(hasMaterialIn(filterOptions.getMaterials()));
        }
        if(filterOptions.getSeason() != null){
            specifications.add(hasSeason(filterOptions.getSeason()));
        }
        if(filterOptions.getRating() != null){
            specifications.add(hasRatingGreaterThan(filterOptions.getRating()));
        }
        if(filterOptions.getSearchPattern() != null){
            specifications.add(matchesSearchPattern(filterOptions.getSearchPattern()));
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
