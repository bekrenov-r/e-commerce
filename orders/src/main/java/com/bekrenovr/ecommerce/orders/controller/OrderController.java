package com.bekrenovr.ecommerce.orders.controller;

import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailedResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(orderService.getById(id));
    }

/*    @GetMapping("/by-customer/{id}")
    public ResponseEntity<List<OrderResponse>> getAllByCustomerId(@PathVariable("id") Integer customerId){
        return orderService.getAllByCustomerId(customerId);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }*/
}
