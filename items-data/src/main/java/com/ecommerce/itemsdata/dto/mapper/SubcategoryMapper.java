package com.ecommerce.itemsdata.dto.mapper;

import com.ecommerce.itemsdata.dto.response.SubcategoryResponse;
import com.ecommerce.itemsdata.model.Subcategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryResponse subcategoryToResponse(Subcategory subcategory);
}
