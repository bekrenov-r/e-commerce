package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.SubcategoryResponse;
import com.bekrenovr.ecommerce.catalog.model.Subcategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryResponse subcategoryToResponse(Subcategory subcategory);
}
