package com.bekrenovr.ecommerce.catalog.model.entity;

import com.bekrenovr.ecommerce.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "unique_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UniqueItem extends AbstractEntity {
    @Column(name = "size")
    private String size;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    @Column(name = "bar_code")
    private String barCode;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "restock_quantity")
    private Integer restockQuantity;

    @Column(name = "reorder_quantity")
    private Integer reorderQuantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
