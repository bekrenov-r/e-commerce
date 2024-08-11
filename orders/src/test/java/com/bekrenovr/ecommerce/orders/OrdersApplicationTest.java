package com.bekrenovr.ecommerce.orders;

import com.bekrenovr.ecommerce.orders.controller.OrderController;
import com.bekrenovr.ecommerce.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrdersApplicationTest {

    OrderController orderController;
    OrderRepository orderRepository;

    @Autowired
    public OrdersApplicationTest(OrderController orderController, OrderRepository orderRepository) {
        this.orderController = orderController;
        this.orderRepository = orderRepository;
    }

//    @Test
//    void validation(){
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//
//        OrderRequest order = new OrderRequest(-1, -3, -10);
//
//        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(order);
//
//        if (!violations.isEmpty()) {
//            for (ConstraintViolation<OrderRequest> violation : violations) {
//                System.out.println(violation.getMessage());
//            }
//        }
//    }

//    @Test
//    void findOrdersByCustomerId(){
//        List<Order> orders = orderRepository.findAllByCustomerId(2);
//        orders.forEach(System.out::println);
//    }

}
