package com.bekrenovr.ecommerce.orders.delivery;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryResponse entityToResponse(Delivery delivery);
    Delivery requestToEntity(DeliveryRequest request);
}
