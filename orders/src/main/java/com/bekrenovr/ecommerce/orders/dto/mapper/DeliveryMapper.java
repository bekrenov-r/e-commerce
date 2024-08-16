package com.bekrenovr.ecommerce.orders.dto.mapper;

import com.bekrenovr.ecommerce.orders.dto.request.DeliveryRequest;
import com.bekrenovr.ecommerce.orders.dto.response.DeliveryResponse;
import com.bekrenovr.ecommerce.orders.model.entity.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryResponse entityToResponse(Delivery delivery);
    Delivery requestToEntity(DeliveryRequest request);
}
