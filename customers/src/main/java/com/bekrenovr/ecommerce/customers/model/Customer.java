package com.bekrenovr.ecommerce.customers.model;

import com.bekrenovr.ecommerce.common.model.Person;
import com.bekrenovr.ecommerce.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends AbstractEntity implements Person {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "is_registered")
    private boolean isRegistered;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ElementCollection(targetClass = UUID.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "customer_wish_list", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "item_id")
    private List<UUID> wishListItems;
}
