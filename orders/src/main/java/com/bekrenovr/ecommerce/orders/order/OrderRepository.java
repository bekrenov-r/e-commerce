package com.bekrenovr.ecommerce.orders.order;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.ORDER_NOT_FOUND;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByCustomerEmail(String email);

    List<Order> findAllByCustomerEmailAndStatus(String email, OrderStatus status);

    default Order findByIdOrThrowDefault(UUID id) {
        return findById(id)
                .orElseThrow(() -> new EcommerceApplicationException(ORDER_NOT_FOUND, id));
    }
}
