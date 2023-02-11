package ru.practicum.ewm_main.error.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ApiError {

    private final HttpStatus status;

    private final String reason;

    private final String message;

    private final LocalDateTime timestamp;
}
