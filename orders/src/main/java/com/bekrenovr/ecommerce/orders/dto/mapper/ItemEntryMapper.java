package com.bekrenovr.ecommerce.orders.dto.mapper;

import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemEntryMapper {
    ItemEntryResponse entityToResponse(ItemEntry itemEntry);
}
