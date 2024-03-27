package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.proxy.CustomerServiceProxy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ItemToDtoMapper {
    @Autowired
    protected CustomerServiceProxy customerServiceProxy;

    @Value("${custom.strategy.popularity.item-is-considered-popular-at-orders-count}")
    private int popularItemOrdersCount;

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "isOnWishList", source = "item", qualifiedByName = "isOnWishList")
    @Mapping(target = "isNew", source = "item", qualifiedByName = "isNew")
    @Mapping(target = "isPopular", source = "item", qualifiedByName = "isPopular")
    public abstract ItemResponse itemToResponse(Item item);

    @Named("isOnWishList")
    protected boolean isOnWishList(Item item){
        // todo: get customerId from security
        UUID customerId = null;
        return customerServiceProxy.isItemOnWishList(item.getId(), customerId);
    }

    @Named("isNew")
    protected boolean isNew(Item item){
        LocalDate createdDate = item.getItemDetails().getCreatedAt().toLocalDate();
        return LocalDate.now().toEpochDay() - createdDate.toEpochDay() > 30;
    }

    @Named("isPopular")
    protected boolean isPopular(Item item){
        return item.getItemDetails().getOrdersCountLastMonth() >= popularItemOrdersCount;
    }
}
