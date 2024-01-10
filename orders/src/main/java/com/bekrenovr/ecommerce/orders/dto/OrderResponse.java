package com.bekrenovr.ecommerce.orders.dto;

import com.bekrenovr.ecommerce.orders.entity.Order;

import java.time.LocalDateTime;

public record OrderResponse(
        Integer id,
        CustomerDTO customer,
        ItemResponse item,
        LocalDateTime orderTimestamp,
        Integer quantity,
        double totalPrice
) {

    public static OrderResponseBuilder builder(){
        return new OrderResponseBuilder();
    }

    public static class OrderResponseBuilder {

        private Integer id;
        private CustomerDTO customer;
        private ItemResponse item;
        private LocalDateTime orderTimestamp;
        private Integer quantity;
        private double totalPrice;

        public OrderResponseBuilder order(Order order){
            this.id = order.getId();
            this.orderTimestamp = order.getOrderTimestamp();
            this.quantity = order.getQuantity();
            this.totalPrice = order.getTotalPrice();
            return this;
        }

        public OrderResponseBuilder customer(CustomerDTO customer){
            this.customer = customer;
            return this;
        }

        public OrderResponseBuilder item(ItemResponse item){
            this.item = item;
            return this;
        }

        public OrderResponse build(){
            return new OrderResponse(id, customer, item, orderTimestamp, quantity, totalPrice);
        }
    }


}
