package com.bekrenovr.ecommerce.orders.dto.mapper;

import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.dto.response.CatalogItem;
import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import com.bekrenovr.ecommerce.orders.proxy.CatalogProxy;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class ItemEntryMapper {
    @Autowired
    protected CatalogProxy catalogProxy;

    public abstract ItemEntryResponse entityToResponse(ItemEntry itemEntry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemId", source = "id")
    public abstract ItemEntry itemResponseToEntity(CatalogItem response, @Context int quantity, @Context String size);

    @Mapping(target = "itemSize", source = "size")
    public abstract ItemEntry requestToEntity(ItemEntryRequest request);

    @AfterMapping
    protected void afterMapping(@MappingTarget ItemEntry itemEntry, @Context int quantity, @Context String size){
        itemEntry.setQuantity(quantity);
        itemEntry.setItemSize(size);
    }

    @AfterMapping
    protected void afterMapping(@MappingTarget ItemEntryResponse response, ItemEntry itemEntry){
        CatalogItem catalogItem = catalogProxy.getItemById(itemEntry.getItemId()).getBody();
        Objects.requireNonNull(catalogItem);
        double totalPrice = multiplyPriceByQuantity(catalogItem.price(), itemEntry.getQuantity());
        double totalPriceAfterDiscount = multiplyPriceByQuantity(catalogItem.priceAfterDiscount(), itemEntry.getQuantity());
        response.setDiscount(catalogItem.discount());
        response.setItemName(catalogItem.name());
        response.setItemPrice(catalogItem.price());
        response.setItemPriceAfterDiscount(catalogItem.priceAfterDiscount());
        response.setTotalPrice(totalPrice);
        response.setTotalPriceAfterDiscount(totalPriceAfterDiscount);
    }

    protected double multiplyPriceByQuantity(double price, int quantity) {
        return BigDecimal.valueOf(price)
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_DOWN)
                .doubleValue();
    }
}
