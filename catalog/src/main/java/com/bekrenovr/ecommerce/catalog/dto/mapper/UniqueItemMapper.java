package com.bekrenovr.ecommerce.catalog.dto.mapper;

import com.bekrenovr.ecommerce.catalog.dto.response.UniqueItemShortResponse;
import com.bekrenovr.ecommerce.catalog.model.entity.UniqueItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniqueItemMapper {
    UniqueItemShortResponse entityToShortResponse(UniqueItem uniqueItem);
}
