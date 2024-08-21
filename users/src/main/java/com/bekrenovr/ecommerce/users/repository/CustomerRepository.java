package com.bekrenovr.ecommerce.users.repository;

import com.bekrenovr.ecommerce.users.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Customer findByEmail(String email);
    boolean existsByEmail(String email);
}
