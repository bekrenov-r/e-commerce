package com.bekrenovr.ecommerce.users.repository;

import com.bekrenovr.ecommerce.users.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByEmail(String email);
}
