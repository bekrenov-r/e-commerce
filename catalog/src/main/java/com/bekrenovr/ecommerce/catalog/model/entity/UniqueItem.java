package com.bekrenovr.ecommerce.catalog.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "unique_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniqueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
