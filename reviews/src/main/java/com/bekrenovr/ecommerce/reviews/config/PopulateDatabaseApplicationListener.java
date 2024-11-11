package com.bekrenovr.ecommerce.reviews.config;

import com.bekrenovr.ecommerce.reviews.model.Review;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class PopulateDatabaseApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.init-file}")
    private String filePath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        mongoTemplate.remove(new Query(), "reviews");
        JSONArray jsonArray = new JSONArray(getReviewsJson());
        List<Review> reviews = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(JSONObject.class::cast)
                .map(this::mapToReview)
                .toList();
        mongoTemplate.insert(reviews, Review.class);
    }

    private String getReviewsJson() {
        try(InputStream is = getClass().getResourceAsStream(filePath)){
            return new String(is.readAllBytes());
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private Review mapToReview(JSONObject json) {
        UUID itemId = UUID.fromString(json.getString("itemId"));
        String customerEmail = json.getString("customerEmail");
        int rating = json.getInt("rating");
        String title = json.getString("title");
        String content = json.optString("content");
        int likes = json.optInt("likes");
        int dislikes = json.optInt("dislikes");
        LocalDateTime timestamp = json.has("createdAt")
                ? LocalDateTime.parse(json.getString("createdAt"))
                : LocalDateTime.now();
        return new Review(null, itemId, customerEmail, rating, title, content, likes, dislikes, timestamp);
    }
}
