package ru.practicum.shareit.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus sc;
    private final String message;

    public ApiException(String message, HttpStatus status) {
        this.message = message;
        this.sc = status;
    }
}
