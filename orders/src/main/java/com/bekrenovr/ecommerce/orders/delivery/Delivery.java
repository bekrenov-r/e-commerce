package com.bekrenovr.ecommerce.orders.delivery;

import com.bekrenovr.ecommerce.common.model.entity.AbstractEntity;
import com.bekrenovr.ecommerce.orders.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Delivery extends AbstractEntity {
    @OneToOne(mappedBy = "delivery")
    private Order order;
}
