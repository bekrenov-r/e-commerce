package com.bekrenovr.ecommerce.orders.order;

import com.bekrenovr.ecommerce.orders.order.dto.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.order.dto.OrderRequest;
import com.bekrenovr.ecommerce.orders.order.dto.OrderResponse;
import com.bekrenovr.ecommerce.orders.util.OrderResponsePageModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "OrderController")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get order by id")
    @ApiResponse(responseCode = "200", description = "Order found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OrderDetailedResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated customer is not order owner",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailedResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(orderService.getById(id));
    }

    @Operation(summary = "Get orders for customer")
    @ApiResponse(responseCode = "200", description = "Page with orders",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OrderResponsePageModel.class)))
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/customer")
    public ResponseEntity<Page<OrderResponse>> getAllForCustomer(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") Integer pageSize
    ){
        return ResponseEntity.ok(orderService.getAllForCustomer(status, pageNumber, pageSize));
    }

    @Operation(summary = "Create order")
    @ApiResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OrderResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body supplied / One or more item don't exist or are unavailable",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Validated OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(orderRequest));
    }

    @Operation(summary = "Cancel order")
    @ApiResponse(responseCode = "200", description = "Order canceled successfully")
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Cannot cancel order with this status",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated customer is not order owner",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public void cancel(@PathVariable UUID id) {
        orderService.cancel(id);
    }
}
