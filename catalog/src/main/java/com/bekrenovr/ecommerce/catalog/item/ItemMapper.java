package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.feign.ReviewsProxy;
import com.bekrenovr.ecommerce.catalog.item.metadata.ItemMetadata;
import com.bekrenovr.ecommerce.catalog.item.sorting.UniqueItemBySizeComparator;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItemMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = UniqueItemMapper.class)
public abstract class ItemMapper {
    @Autowired
    protected ReviewsProxy reviewsProxy;
    @Autowired
    protected UniqueItemBySizeComparator uniqueItemComparator;

    @Mapping(target = "brand", source = "brand.name")
    public abstract ItemResponse itemToResponse(Item item, @Context ItemMetadata metadata);

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "reviews", source = "item", qualifiedByName = "getReviews")
    public abstract ItemDetailedResponse itemToDetailedResponse(Item item, @Context ItemMetadata metadata);

    @AfterMapping
    protected void afterMapping(@MappingTarget ItemDetailedResponse itemDetailedResponse, @Context ItemMetadata metadata){
        itemDetailedResponse.setMetadata(metadata);
        itemDetailedResponse.getUniqueItems().sort(uniqueItemComparator);
    }

    @AfterMapping
    protected void afterMapping(@MappingTarget ItemResponse itemResponse, @Context ItemMetadata metadata){
        itemResponse.setMetadata(metadata);
        itemResponse.getUniqueItems().sort(uniqueItemComparator);
    }

    @Named("getReviews")
    protected List<?> getReviews(Item item){
        return reviewsProxy.getReviewsForItem(item.getId());
    }
}
