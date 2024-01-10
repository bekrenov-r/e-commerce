package com.bekrenovr.ecommerce.orders.repository;

import com.bekrenovr.ecommerce.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("from Order order where order.customerId = :customerId")
    List<Order> findAllByCustomerId(Integer customerId);

}
