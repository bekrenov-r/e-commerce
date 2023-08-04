package com.ecommerce.ordersdata.controller;

import com.ecommerce.ordersdata.config.OrdersConfiguration;
import com.ecommerce.ordersdata.dto.OrderRequest;
import com.ecommerce.ordersdata.dto.OrderResponse;
import com.ecommerce.ordersdata.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
