package com.bekrenovr.ecommerce.orders.order.itementry;

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

    @Column(name = "item_size")
    private String itemSize;

    @Column(name = "quantity")
    private int quantity;
}
