package com.ecommerce.itemsdata.dto.mapper;

import com.ecommerce.itemsdata.dto.response.ItemResponse;
import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.service.ItemMetadataService;
import com.ecommerce.itemsdata.util.ImagesUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ItemToDtoMapper {

    @Autowired
    protected ImagesUtil imagesUtil;
    @Autowired
    protected ItemMetadataService itemMetadataService;
    @Autowired
    protected ItemMappingDataProcessor itemMappingDataProcessor;

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "discount", source = "item.discount")
    @Mapping(target = "priceAfterDiscount", source = "item.priceAfterDiscount")
    @Mapping(target = "images", expression = "java(imagesUtil.itemImagesToBytesArrays(item.getImages()))")
    @Mapping(target = "brand", source = "item.brand")
    @Mapping(target = "rating", source = "item.rating")
    @Mapping(target = "colors", expression = "java(itemMappingDataProcessor.allColorsForItem(item))")
    @Mapping(target = "isOnWishList", expression = "java(itemMappingDataProcessor.isItemOnWishList(item.getId()))")
    @Mapping(target = "isNew", expression = "java(itemMetadataService.isItemNew(item.getId()))")
    @Mapping(target = "isPopular", expression = "java(itemMetadataService.isItemPopular(item.getId()))")
    public abstract ItemResponse itemToResponse(Item item);

}
