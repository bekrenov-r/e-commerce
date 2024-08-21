package com.bekrenovr.ecommerce.users.model.entity;

import com.bekrenovr.ecommerce.common.entity.AbstractEntity;
import com.bekrenovr.ecommerce.users.model.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
