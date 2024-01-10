package com.bekrenovr.ecommerce.orders.dto;

import com.bekrenovr.ecommerce.orders.entity.Order;

public class OrderToDtoMapper {

    public static OrderResponse orderResponse(Order order, CustomerDTO customer, ItemResponse item){
        return OrderResponse.builder()
                .order(order)
                .customer(customer)
                .item(item)
                .build();
    }

}
