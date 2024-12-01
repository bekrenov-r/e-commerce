package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.brand.Brand;
import com.bekrenovr.ecommerce.catalog.brand.BrandRepository;
import com.bekrenovr.ecommerce.catalog.category.Category;
import com.bekrenovr.ecommerce.catalog.category.CategoryMapper;
import com.bekrenovr.ecommerce.catalog.category.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.category.subcategory.Subcategory;
import com.bekrenovr.ecommerce.catalog.item.metadata.ItemMetadata;
import com.bekrenovr.ecommerce.catalog.item.sorting.UniqueItemBySizeComparator;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItemMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = { UniqueItemMapper.class, CategoryMapper.class })
public abstract class ItemMapper {
    @Autowired
    protected UniqueItemBySizeComparator uniqueItemComparator;
    @Autowired
    protected BrandRepository brandRepository;
    @Autowired
    protected CategoryRepository categoryRepository;

    @Mapping(target = "brand", source = "brand.name")
    public abstract ItemResponse itemToResponse(Item item, @Context ItemMetadata metadata);

    @Mapping(target = "brand", source = "brand.name")
    public abstract ItemDetailedResponse itemToDetailedResponse(Item item, @Context ItemMetadata metadata);

    @Mapping(target = "brand", source = "brandId", qualifiedByName = "mapBrand")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    @Mapping(target = "subcategory", source = ".", conditionExpression = "java(request.subcategoryId() != null)",
            qualifiedByName = "mapSubcategory", dependsOn = "category")
    @Mapping(target = "priceAfterDiscount", expression = "java(Item.calculatePriceAfterDiscount(request.price(), request.discount()))")
    public abstract Item requestToItem(ItemRequest request);

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

    @Named("mapBrand")
    protected Brand mapBrand(UUID brandId) {
        return brandRepository.findByIdOrThrowDefault(brandId);
    }

    @Named("mapCategory")
    protected Category mapCategory(UUID categoryId) {
        return categoryRepository.findByIdOrThrowDefault(categoryId);
    }

    @Named("mapSubcategory")
    protected Subcategory mapSubcategory(ItemRequest request) {
        return categoryRepository.findByIdOrThrowDefault(request.categoryId()).findSubcategory(request.subcategoryId());
    }
}
