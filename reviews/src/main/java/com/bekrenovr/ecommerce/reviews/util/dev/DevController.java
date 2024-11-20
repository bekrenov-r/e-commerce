package com.bekrenovr.ecommerce.reviews.util.dev;

import com.bekrenovr.ecommerce.reviews.review.Review;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController("/reviews/dev")
@RequiredArgsConstructor
@Profile("dev")
public class DevController {
    private final MongoTemplate mongoTemplate;

    @PostMapping("/populate-database")
    public void populateDatabase() {
        mongoTemplate.remove(new Query(), "reviews");
        JSONArray jsonArray = new JSONArray(getReviewsJson());
        List<Review> reviews = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(JSONObject.class::cast)
                .map(this::mapToReview)
                .toList();
        mongoTemplate.insert(reviews, Review.class);
    }

    private String getReviewsJson() {
        try(InputStream is = getClass().getResourceAsStream("/init.json")){
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
