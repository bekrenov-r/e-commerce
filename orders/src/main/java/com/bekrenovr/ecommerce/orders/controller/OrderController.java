package com.bekrenovr.ecommerce.orders.controller;

import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.dto.response.OrderResponse;
import com.bekrenovr.ecommerce.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/customer/{id}")
    public ResponseEntity<Page<OrderResponse>> getAllByCustomerId(
            @PathVariable("id") UUID customerId,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") int pageSize
    ){
        return ResponseEntity.ok(orderService.getAllByCustomerId(customerId, pageNumber, pageSize));
    }

/*    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderRequest));
    }*/
}
