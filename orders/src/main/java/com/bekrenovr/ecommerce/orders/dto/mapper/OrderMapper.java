package com.bekrenovr.ecommerce.orders.dto.mapper;

import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.dto.response.OrderResponse;
import com.bekrenovr.ecommerce.orders.model.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ItemEntryMapper.class, DeliveryMapper.class })
public interface OrderMapper {
    OrderDetailedResponse entityToDetailedResponse(Order order);
    OrderResponse entityToResponse(Order order);
}
