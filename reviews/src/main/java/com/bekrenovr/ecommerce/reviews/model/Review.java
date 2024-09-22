package com.bekrenovr.ecommerce.reviews.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "reviews")
@Data
@AllArgsConstructor
public class Review {
    @Id
    private String id;
    private UUID itemId;
    private String customerEmail;
    private int rating;
    private String title;
    private String content;
    private int likes;
    private int dislikes;
    private LocalDateTime createdAt;
}
