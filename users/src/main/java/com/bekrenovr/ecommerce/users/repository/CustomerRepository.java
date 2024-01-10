package com.bekrenovr.ecommerce.users.repository;

import com.bekrenovr.ecommerce.users.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
