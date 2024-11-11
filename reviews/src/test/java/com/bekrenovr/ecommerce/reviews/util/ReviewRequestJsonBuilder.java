package com.bekrenovr.ecommerce.reviews.util;

import com.bekrenovr.ecommerce.common.util.TestUtil;
import org.json.JSONObject;

import java.util.UUID;

public class ReviewRequestJsonBuilder {
    private static final String RESOURCE_PATH = "/review-request.json";
    private JSONObject json;

    private ReviewRequestJsonBuilder() {
        String template = TestUtil.getResourceAsString(RESOURCE_PATH);
        this.json = new JSONObject(template);
    }

    public static ReviewRequestJsonBuilder create() {
        return new ReviewRequestJsonBuilder();
    }

    public ReviewRequestJsonBuilder itemId(UUID uuid) {
        json.put("itemId", uuid.toString());
        return this;
    }

    public ReviewRequestJsonBuilder rating(int rating) {
        json.put("rating", rating);
        return this;
    }

    public ReviewRequestJsonBuilder title(String title) {
        json.put("title", title);
        return this;
    }

    public ReviewRequestJsonBuilder content(String content) {
        json.put("content", content);
        return this;
    }

    public JSONObject build() {
        return json;
    }
}
