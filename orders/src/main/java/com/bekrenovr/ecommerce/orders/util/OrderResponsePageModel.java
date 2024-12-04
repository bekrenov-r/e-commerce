package com.bekrenovr.ecommerce.orders.util;

import com.bekrenovr.ecommerce.orders.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class OrderResponsePageModel extends PageImpl<OrderResponse> {
    @ArraySchema(schema = @Schema(implementation = OrderResponse.class))
    private List<OrderResponse> content;

    public OrderResponsePageModel(List<OrderResponse> content) {
        super(content);
    }
}
