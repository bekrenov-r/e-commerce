package com.bekrenovr.ecommerce.orders.model.entity;

import com.bekrenovr.ecommerce.common.entity.AbstractEntity;
import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Order extends AbstractEntity {
    @Column(name = "customer_id")
    private UUID customerId;

    @OneToMany(mappedBy = "order")
    private Set<ItemEntry> itemEntries;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_price_after_discount")
    private double totalPriceAfterDiscount;

    @Column(name = "number")
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
}
