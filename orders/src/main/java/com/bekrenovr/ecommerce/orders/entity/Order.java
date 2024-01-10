package com.bekrenovr.ecommerce.orders.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "order_timestamp")
    private LocalDateTime orderTimestamp;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private double totalPrice;

    public Order(Integer customerId, Integer itemId, LocalDateTime orderTimestamp, int quantity, double totalPrice) {
        this.customerId = customerId;
        this.itemId = itemId;
        this.orderTimestamp = orderTimestamp;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
