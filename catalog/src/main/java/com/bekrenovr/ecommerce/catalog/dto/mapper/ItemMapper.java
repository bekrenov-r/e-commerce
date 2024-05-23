package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemDetailedResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.ReviewResponse;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.model.entity.UniqueItem;
import com.bekrenovr.ecommerce.catalog.proxy.CustomerServiceProxy;
import com.bekrenovr.ecommerce.catalog.proxy.ReviewServiceProxy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {
    @Autowired
    protected CustomerServiceProxy customerServiceProxy;
    @Autowired
    protected ReviewServiceProxy reviewServiceProxy;

    @Value("${custom.strategy.popularity.item-is-considered-popular-at-orders-count}")
    private int popularItemOrdersCount;

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "isOnWishList", source = "item", qualifiedByName = "isOnWishList")
    @Mapping(target = "isNew", source = "item", qualifiedByName = "isNew")
    @Mapping(target = "isPopular", source = "item", qualifiedByName = "isPopular")
    public abstract ItemResponse itemToResponse(Item item);

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "isOnWishList", source = "item", qualifiedByName = "isOnWishList")
    @Mapping(target = "isNew", source = "item", qualifiedByName = "isNew")
    @Mapping(target = "isPopular", source = "item", qualifiedByName = "isPopular")
    @Mapping(target = "isAvailable", source = "item", qualifiedByName = "isAvailable")
    @Mapping(target = "sizes", source = "item", qualifiedByName = "mapSizes")
    @Mapping(target = "availableSizes", source = "item", qualifiedByName = "mapAvailableSizes")
    @Mapping(target = "reviews", source = "item", qualifiedByName = "getReviews")
    public abstract ItemDetailedResponse itemToDetailedResponse(Item item);

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

    @Named("isAvailable")
    protected boolean isAvailable(Item item){
        int itemQuantity = item.getUniqueItems().stream()
                .mapToInt(UniqueItem::getQuantity)
                .sum();
        return itemQuantity > 0;
    }

    @Named("getReviews")
    protected List<ReviewResponse> getReviews(Item item){
        return reviewServiceProxy.getReviewsForItem(item.getId());
    }

    @Named("mapSizes")
    protected List<String> mapSizes(Item item){
        return item.getUniqueItems().stream()
                .map(UniqueItem::getSize)
                .toList();
    }

    @Named("mapAvailableSizes")
    protected List<String> mapAvailableSizes(Item item){
        return item.getUniqueItems().stream()
                .filter(ui -> ui.getQuantity() != 0)
                .map(UniqueItem::getSize)
                .toList();
    }
}
