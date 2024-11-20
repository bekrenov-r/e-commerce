package com.bekrenovr.ecommerce.catalog.kafka.rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.cloudinary.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Log4j2
public class RatingEventConsumer {
    private final RatingEventHandler eventHandler;

    @KafkaListener(topics = "rating-events")
    public void onRatingEvent(ConsumerRecord<Object, String> record) {
        log.info("Consumed rating event: {}", record);
        RatingEvent event = this.deserializeEvent(record);
        eventHandler.handle(event);
    }

    private RatingEvent deserializeEvent(ConsumerRecord<Object, String> record) {
        JSONObject json = new JSONObject(record.value());
        UUID itemId = UUID.fromString(json.getString("itemId"));
        double averageRating = json.getDouble("averageRating");
        return new RatingEvent(itemId, averageRating);
    }
}
