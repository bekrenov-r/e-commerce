package com.ecommerce.itemsdata.dto.mapper;

import com.ecommerce.itemsdata.dto.response.CategoryResponse;
import com.ecommerce.itemsdata.model.Category;
import com.ecommerce.itemsdata.util.ImageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    @Autowired
    protected ImageUtils imageUtils;

    @Mapping(target = "image", expression = "java(imageUtils.imagePathToByteArray(category.getImagePath()))")
    public abstract CategoryResponse categoryToResponse(Category category);
}
