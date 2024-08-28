package com.bekrenovr.ecommerce.orders.model.entity;

import com.bekrenovr.ecommerce.common.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cart extends AbstractEntity {
    @Column(name = "customer_email")
    private String customerEmail;

    @OneToMany
    @JoinTable(
            name = "cart_item_entry",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "item_entry_id")
    )
    private List<ItemEntry> itemEntries;
}
