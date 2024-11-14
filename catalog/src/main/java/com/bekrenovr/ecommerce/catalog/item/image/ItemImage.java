package com.bekrenovr.ecommerce.catalog.item.image;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_image")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemImage extends AbstractEntity {
    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
