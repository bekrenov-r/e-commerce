package com.bekrenovr.ecommerce.orders.model.entity;

import com.bekrenovr.ecommerce.common.model.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "item_entry")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ItemEntry extends AbstractEntity {
    @Column(name = "item_id")
    private UUID itemId;

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
