package ru.practicum.ewm_main.error.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiError {

    private HttpStatus status;

    private final String reason;

    private final String message;

    private final LocalDateTime timestamp;

    public ApiError(String reason, String message, LocalDateTime timestamp) {
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }
}
