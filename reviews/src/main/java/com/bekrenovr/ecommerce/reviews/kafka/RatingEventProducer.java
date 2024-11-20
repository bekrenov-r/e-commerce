package com.bekrenovr.ecommerce.reviews.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class RatingEventProducer {
    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaTemplate<Object, String> kafkaTemplate;

    public void produce(RatingEvent event) {
        log.info("Sending event {} to topic '{}'", event, topic);
        String json = this.serializeEvent(event);
        ProducerRecord<Object, String> record = new ProducerRecord<>(topic, null, null, json);
        kafkaTemplate.send(record)
                .whenComplete((res, ex) -> {
                    if(ex != null) {
                        log.error(ex.getMessage(), ex);
                    }
                });
    }

    private String serializeEvent(RatingEvent event) {
        try {
            return new ObjectMapper().writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
