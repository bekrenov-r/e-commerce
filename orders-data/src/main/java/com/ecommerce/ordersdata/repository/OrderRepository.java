package com.ecommerce.ordersdata.repository;

import com.ecommerce.ordersdata.entity.Order;
import jakarta.ws.rs.QueryParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("from Order order where order.customerId = :customerId")
    List<Order> findAllByCustomerId(Integer customerId);

}
