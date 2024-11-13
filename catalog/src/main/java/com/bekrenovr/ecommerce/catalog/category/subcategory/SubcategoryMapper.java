package com.bekrenovr.ecommerce.catalog.category.subcategory;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryResponse subcategoryToResponse(Subcategory subcategory);
}
