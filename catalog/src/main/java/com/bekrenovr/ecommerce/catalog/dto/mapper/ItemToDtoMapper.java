package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.util.ImageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ItemToDtoMapper {

    @Autowired
    protected ImageUtils imageUtils;
    @Autowired
    protected ItemMappingService itemMappingService;

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "discount", source = "item.discount")
    @Mapping(target = "priceAfterDiscount", source = "item.priceAfterDiscount")
    @Mapping(target = "images", expression = "java(imageUtils.convertAllItemImages(item.getImages()))")
    @Mapping(target = "brand", source = "item.brand.name")
    @Mapping(target = "rating", source = "item.rating")
    @Mapping(target = "colors", expression = "java(itemMappingService.allColorsForItem(item))")
    @Mapping(target = "isOnWishList", expression = "java(itemMappingService.isItemOnWishList(item.getId()))")
    @Mapping(target = "isNew", expression = "java(itemMappingService.isItemNew(item))")
    @Mapping(target = "isPopular", expression = "java(itemMappingService.isItemPopular(item))")
    public abstract ItemResponse itemToResponse(Item item);

}
