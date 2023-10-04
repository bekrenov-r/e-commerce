package com.ecommerce.itemsdata.util.dev;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetail {

    private LocalDateTime timestamp;

    private HttpStatusCode error;

    private int statusCode;

    private String message;


    public ErrorDetail(LocalDateTime timestamp, HttpStatusCode error, String message) {
        this.timestamp = timestamp;
        this.error = error;
        this.statusCode = error.value();
        this.message = message;
    }
}
