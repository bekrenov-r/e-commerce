package com.ecommerce.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErrorDetail {

    private LocalDateTime timestamp;

    private HttpStatusCode error;

    private int statusCode;

    private String message;

    private String source;

    public ErrorDetail(LocalDateTime timestamp, HttpStatusCode error, String message, String source) {
        this.timestamp = timestamp;
        this.error = error;
        this.statusCode = error.value();
        this.message = message;
        this.source = source;
    }
}
