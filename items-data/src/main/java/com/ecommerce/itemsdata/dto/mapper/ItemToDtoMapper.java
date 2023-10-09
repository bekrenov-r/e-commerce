package com.ecommerce.itemsdata.dto.mapper;

import com.ecommerce.itemsdata.dto.response.ItemResponse;
import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.util.ImageUtils;
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
    @Mapping(target = "brand", source = "item.brand.name")
    @Mapping(target = "rating", source = "item.rating")
    @Mapping(target = "colors", expression = "java(itemMappingService.allColorsForItem(item))")
    @Mapping(target = "isOnWishList", expression = "java(itemMappingService.isItemOnWishList(item.getId()))")
    @Mapping(target = "isNew", expression = "java(itemMappingService.isItemNew(item))")
    @Mapping(target = "isPopular", expression = "java(itemMappingService.isItemPopular(item))")
    public abstract ItemResponse itemToResponse(Item item);

}
