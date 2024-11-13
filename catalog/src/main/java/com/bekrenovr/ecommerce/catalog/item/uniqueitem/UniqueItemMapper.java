package com.bekrenovr.ecommerce.catalog.item.uniqueitem;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniqueItemMapper {
    UniqueItemResponse entityToShortResponse(UniqueItem uniqueItem);
}
