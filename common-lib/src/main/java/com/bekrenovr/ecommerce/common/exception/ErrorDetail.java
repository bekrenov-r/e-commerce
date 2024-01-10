package com.bekrenovr.ecommerce.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ErrorDetail {
    private LocalDateTime timestamp;
    private HttpStatusCode error;
    private String message;

    public ErrorDetail(LocalDateTime timestamp, HttpStatusCode error, String message) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
    }
}
