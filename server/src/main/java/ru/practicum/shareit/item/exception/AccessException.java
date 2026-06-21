package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exception.ApiException;

public class AccessException extends ApiException {
    public AccessException() {
        super("403", HttpStatus.FORBIDDEN);
    }
}
