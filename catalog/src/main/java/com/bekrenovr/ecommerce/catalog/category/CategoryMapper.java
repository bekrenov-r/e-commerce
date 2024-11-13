package com.bekrenovr.ecommerce.catalog.category;

import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import com.bekrenovr.ecommerce.catalog.util.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    @Value("${custom.category.img-path-prefix.men}")
    private String imgPrefixMen;
    @Value("${custom.category.img-path-prefix.women}")
    private String imgPrefixWomen;

    public CategoryResponse categoryToResponse(Category category){
        return new CategoryResponse(category.getId(), category.getName());
    };

    public byte[] mapCategoryImage(String imageName, Gender gender){
        String imgPathPrefix = switch(gender){
            case MEN -> imgPrefixMen;
            case WOMEN -> imgPrefixWomen;
        };
        return ImageUtils.imageToByteArray(imgPathPrefix + imageName);
    }
}
