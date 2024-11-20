package com.bekrenovr.ecommerce.catalog.kafka.order;

import com.bekrenovr.ecommerce.catalog.kafka.order.model.OrderEvent;
import com.bekrenovr.ecommerce.catalog.kafka.order.model.OrderEventStatus;
import com.bekrenovr.ecommerce.catalog.kafka.order.model.OrderItemEntry;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final OrderEventHandler eventHandler;

    @KafkaListener(topics = "order-events")
    public void onOrderEvent(ConsumerRecord<Object, String> record){
        log.info("Consumed order event: {}", record);
        OrderEvent orderEvent = this.deserializeEvent(record.value());

        if(orderEvent.getStatus().equals(OrderEventStatus.ACCEPTED)) {
            eventHandler.handleOrderCreation(orderEvent);
        } else if(orderEvent.getStatus().equals(OrderEventStatus.CANCELLED)){
            eventHandler.handleOrderCancellation(orderEvent);
        }
    }

    private OrderEvent deserializeEvent(String recordValue) {
        DocumentContext json = JsonPath.parse(recordValue);
        UUID orderId = UUID.fromString(json.read("$.orderId"));
        OrderEventStatus status = OrderEventStatus.valueOf(json.read("$.status"));
        List<OrderItemEntry> itemEntries = this.parseItemEntries(json);
        return new OrderEvent(orderId, status, itemEntries);
    }

    private List<OrderItemEntry> parseItemEntries(DocumentContext json) {
        List<Map<String, Object>> entries = json.read("$.itemEntries");
        return entries.stream()
                .map(this::parseItemEntry)
                .collect(Collectors.toList());
    }

    private OrderItemEntry parseItemEntry(Map<String, Object> entry) {
        UUID itemId = UUID.fromString((String) entry.get("itemId"));
        int quantity = (int) entry.get("quantity");
        String size = (String) entry.get("size");
        return new OrderItemEntry(itemId, quantity, size);
    }
}
