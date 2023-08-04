package com.ecommerce.ordersdata;

import com.ecommerce.common.exception.ErrorDetail;
import com.ecommerce.ordersdata.controller.OrderController;
import com.ecommerce.ordersdata.dto.ItemResponse;
import com.ecommerce.ordersdata.dto.OrderRequest;
import com.ecommerce.ordersdata.dto.OrderResponse;
import com.ecommerce.ordersdata.entity.Order;
import com.ecommerce.ordersdata.proxy.ItemProxy;
import com.ecommerce.ordersdata.repository.OrderRepository;
import com.netflix.discovery.converters.Auto;
import feign.FeignException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class OrdersDataApplicationTest {

    OrderController orderController;
    OrderRepository orderRepository;

    @Autowired
    public OrdersDataApplicationTest(OrderController orderController, OrderRepository orderRepository) {
        this.orderController = orderController;
        this.orderRepository = orderRepository;
    }

    @Test
    void validation(){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        OrderRequest order = new OrderRequest(-1, -3, -10);

        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(order);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<OrderRequest> violation : violations) {
                System.out.println(violation.getMessage());
            }
        }
    }

    @Test
    void findOrdersByCustomerId(){
        List<Order> orders = orderRepository.findAllByCustomerId(2);
        orders.forEach(System.out::println);
    }

}
