package com.ecommerce.itemsdata.dto.mapper;

import com.ecommerce.itemsdata.dto.response.CategoryResponse;
import com.ecommerce.itemsdata.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SubcategoryMapper.class})
public interface CategoryMapper {
    CategoryResponse categoryToResponse(Category category);
}
