package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.common.ResponseSource;
import com.bekrenovr.ecommerce.orders.dto.*;
import com.bekrenovr.ecommerce.orders.entity.Order;
import com.bekrenovr.ecommerce.orders.proxy.CustomerProxy;
import com.bekrenovr.ecommerce.orders.proxy.ItemProxy;
import com.bekrenovr.ecommerce.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerProxy customerProxy;
    private final ItemProxy itemProxy;
    private final ResponseSource responseSource;

    public OrderService(OrderRepository orderRepository, CustomerProxy customerProxy, ItemProxy itemProxy, ResponseSource responseSource) {
        this.orderRepository = orderRepository;
        this.customerProxy = customerProxy;
        this.itemProxy = itemProxy;
        this.responseSource = responseSource;
    }

    public ResponseEntity<OrderResponse> getById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No order found for id: " + id));
        CustomerDTO customerDTO = customerProxy.getById(order.getCustomerId()).getBody();
        ItemResponse itemResponse = itemProxy.getById(order.getItemId()).getBody();

        OrderResponse orderResponse = OrderToDtoMapper.orderResponse(order, customerDTO, itemResponse);

        return ResponseEntity
                .ok()
                .header("Environment", responseSource.toString())
                .body(orderResponse);
    }

    public ResponseEntity<List<OrderResponse>> getAllByCustomerId(Integer customerId) {
        List<Order> orders = orderRepository.findAllByCustomerId(customerId);
        CustomerDTO customerDTO = customerProxy.getById(customerId).getBody();

        List<Integer> itemIds = orders.stream().map(Order::getItemId).toList();
        List<ItemResponse> items = itemProxy.getItemsByIds(itemIds).getBody();

        List<OrderResponse> orderResponses =
                orders.stream()
                        .map(order -> {
                            int itemId = order.getItemId();
                            for(ItemResponse item : items){
                                if(item.id().equals(itemId)){
                                    return OrderToDtoMapper.orderResponse(order, customerDTO, item);
                                }
                            }
                            return null;
                        })
                        .toList();
        return ResponseEntity.ok(orderResponses);
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
    }
}
