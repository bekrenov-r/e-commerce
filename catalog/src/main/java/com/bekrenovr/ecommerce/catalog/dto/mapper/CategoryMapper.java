package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.CategoryResponse;
import com.bekrenovr.ecommerce.catalog.model.Category;
import com.bekrenovr.ecommerce.catalog.model.Gender;
import com.bekrenovr.ecommerce.catalog.util.ImageUtils;
import com.bekrenovr.ecommerce.catalog.util.request.RequestUtils;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    @Autowired
    protected ImageUtils imageUtils;
    @Autowired
    protected RequestUtils requestUtils;

    @Value("${custom.category.img-path-prefix.men}")
    private String IMG_PREFIX_MEN;
    @Value("${custom.category.img-path-prefix.women}")
    private String IMG_PREFIX_WOMEN;

    @Mapping(target = "image", source = "imageName", qualifiedByName = "mapImage")
    public abstract CategoryResponse categoryToResponse(Category category, @Context Gender gender);

    @Named("mapImage")
    public byte[] mapImage(String imageName, @Context Gender gender){
        String imgPathPrefix = switch(gender){
            case MEN -> IMG_PREFIX_MEN;
            case WOMEN -> IMG_PREFIX_WOMEN;
        };
        return imageUtils.imagePathToByteArray(imgPathPrefix + imageName);
    }
}
