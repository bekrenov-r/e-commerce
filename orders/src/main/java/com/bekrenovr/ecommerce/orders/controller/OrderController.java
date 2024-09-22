package com.bekrenovr.ecommerce.orders.controller;

import com.bekrenovr.ecommerce.orders.dto.request.OrderRequest;
import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.dto.response.OrderResponse;
import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;
import com.bekrenovr.ecommerce.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailedResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/customer")
    public ResponseEntity<Page<OrderResponse>> getAllForCustomer(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") Integer pageSize
    ){
        return ResponseEntity.ok(orderService.getAllForCustomer(status, pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Validated OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderRequest));
    }
}
