package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemImageResponse;
import com.bekrenovr.ecommerce.catalog.model.entity.ItemImage;
import com.bekrenovr.ecommerce.catalog.util.ImageUtils;
import org.springframework.stereotype.Component;

@Component
public class ItemImageMapper {
    public ItemImageResponse entityToResponse(ItemImage entity){
        return new ItemImageResponse(
                entity.getId(),
                ImageUtils.imageToByteArray(entity.getPath())
        );
    }
}
