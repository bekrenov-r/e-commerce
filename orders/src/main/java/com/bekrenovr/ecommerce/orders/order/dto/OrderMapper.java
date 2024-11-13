package com.bekrenovr.ecommerce.orders.order.dto;

import com.bekrenovr.ecommerce.orders.delivery.DeliveryMapper;
import com.bekrenovr.ecommerce.orders.order.Order;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ItemEntryMapper.class, DeliveryMapper.class })
public interface OrderMapper {
    OrderDetailedResponse entityToDetailedResponse(Order order);
    OrderResponse entityToResponse(Order order);
}
