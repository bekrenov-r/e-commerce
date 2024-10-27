package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemDetailedResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemMetadata;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.ReviewResponse;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.proxy.ReviewServiceProxy;
import com.bekrenovr.ecommerce.catalog.util.sort.UniqueItemBySizeComparator;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = UniqueItemMapper.class)
public abstract class ItemMapper {
    @Autowired
    protected ReviewServiceProxy reviewServiceProxy;
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
    protected List<ReviewResponse> getReviews(Item item){
        return reviewServiceProxy.getReviewsForItem(item.getId());
    }
}
