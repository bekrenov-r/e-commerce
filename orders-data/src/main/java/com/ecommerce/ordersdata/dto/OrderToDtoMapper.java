package com.ecommerce.ordersdata.dto;

import com.ecommerce.ordersdata.dto.CustomerDTO;
import com.ecommerce.ordersdata.dto.ItemResponse;
import com.ecommerce.ordersdata.dto.OrderResponse;
import com.ecommerce.ordersdata.entity.Order;

public class OrderToDtoMapper {

    public static OrderResponse orderResponse(Order order, CustomerDTO customer, ItemResponse item){
        return OrderResponse.builder()
                .order(order)
                .customer(customer)
                .item(item)
                .build();
    }

}
