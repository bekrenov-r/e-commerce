package com.bekrenovr.ecommerce.orders.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "item_entry")
@IdClass(ItemEntryId.class)
@Data
@NoArgsConstructor
public class ItemEntry {
    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Id
    @Column(name = "item_id")
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_size")
    private String itemSize;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "discount")
    private double discount;

    @Column(name = "item_price")
    private double itemPrice;

    @Column(name = "item_price_after_discount")
    private double itemPriceAfterDiscount;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_price_after_discount")
    private double totalPriceAfterDiscount;
}
