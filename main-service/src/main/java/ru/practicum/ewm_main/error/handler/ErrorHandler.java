package ru.practicum.ewm_main.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm_main.error.exception.ConflictException;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.error.model.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBadRequestException(MethodArgumentNotValidException e) {
        log.error("Bad request exception.");
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,
                "Не соблюдены условия для запрошенной операции.", e.getMessage(), LocalDateTime.now()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(final ConflictException e) {
        log.error("Conflict exception.");
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Запрос противоречит установленным ограничениям.",
                e.getMessage(), LocalDateTime.now()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException e) {
        log.error("Not found exception.");
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, "Требуемый объект не был найден.",
                e.getMessage(), LocalDateTime.now()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleException(Throwable e) {
        log.error("Internal server error.");
        return buildResponseEntity(new ApiError("Недопустимый запрос.",
                e.getMessage(), LocalDateTime.now()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
