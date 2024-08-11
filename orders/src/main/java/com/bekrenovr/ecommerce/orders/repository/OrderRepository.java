package com.bekrenovr.ecommerce.orders.repository;

import com.bekrenovr.ecommerce.orders.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("from Order order where order.customerId = :customerId")
    List<Order> findAllByCustomerId(Integer customerId);

}
