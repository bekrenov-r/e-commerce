package com.bekrenovr.ecommerce.orders.controller;

import com.bekrenovr.ecommerce.orders.dto.OrderRequest;
import com.bekrenovr.ecommerce.orders.dto.OrderResponse;
import com.bekrenovr.ecommerce.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable("id") Integer id){
        return orderService.getById(id);
    }

    @GetMapping("/by-customer/{id}")
    public ResponseEntity<List<OrderResponse>> getAllByCustomerId(@PathVariable("id") Integer customerId){
        return orderService.getAllByCustomerId(customerId);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }
}
