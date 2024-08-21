package com.bekrenovr.ecommerce.orders.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class ItemEntryMapper {
    public abstract ItemEntryResponse entityToResponse(ItemEntry itemEntry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemId", source = "id")
    @Mapping(target = "itemName", source = "name")
    @Mapping(target = "itemPrice", source = "price")
    @Mapping(target = "itemPriceAfterDiscount", source = "priceAfterDiscount")
    public abstract ItemEntry itemResponseToEntity(ItemResponse request, @Context int quantity, @Context Size size);

    @AfterMapping
    protected void afterMapping(@MappingTarget ItemEntry itemEntry, @Context int quantity, @Context Size size){
        itemEntry.setQuantity(quantity);
        itemEntry.setItemSize(size.getSizeValue());
        itemEntry.setTotalPrice(itemEntry.getQuantity() * itemEntry.getItemPrice());
        itemEntry.setTotalPriceAfterDiscount(itemEntry.getQuantity() * itemEntry.getItemPriceAfterDiscount());
    }
}
