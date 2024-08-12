package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.common.util.PageUtil;
import com.bekrenovr.ecommerce.orders.dto.mapper.OrderMapper;
import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.dto.response.OrderResponse;
import com.bekrenovr.ecommerce.orders.model.entity.Order;
import com.bekrenovr.ecommerce.orders.proxy.CustomerProxy;
import com.bekrenovr.ecommerce.orders.proxy.ItemProxy;
import com.bekrenovr.ecommerce.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerProxy customerProxy;
    private final ItemProxy itemProxy;

    public OrderDetailedResponse getById(UUID id) {
        Order order = orderRepository.findByIdOrThrowDefault(id);
        return orderMapper.entityToDetailedResponse(order);
    }

    public Page<OrderResponse> getAllByCustomerId(UUID customerId, int pageNumber, int pageSize) {
        List<OrderResponse> orders = orderRepository.findAllByCustomerId(customerId)
                .stream()
                .map(orderMapper::entityToResponse)
                .toList();
        return PageUtil.paginateList(orders, pageNumber, pageSize);
    }

    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest){
        CustomerDTO customerDTO = customerProxy.getById(orderRequest.customerId()).getBody();
        ItemResponse itemResponse = itemProxy.getById(orderRequest.itemId()).getBody();

        double totalPrice = itemResponse.priceAfterDiscount() * orderRequest.quantity();
        Order order = new Order(
                orderRequest.customerId(),
                orderRequest.itemId(),
                LocalDateTime.now(),
                orderRequest.quantity(),
                totalPrice
        );
        Order savedOrder = orderRepository.save(order);
        OrderResponse orderResponse = OrderToDtoMapper.orderResponse(savedOrder, customerDTO, itemResponse);
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(orderResponse.id())
                                .toUri()
                )
                .header("Environment", responseSource.toString())
                .body(orderResponse);
    }*/
}
