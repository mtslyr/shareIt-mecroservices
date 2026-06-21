package ru.practicum.shareit.common.exception;

import org.springframework.http.HttpStatus;

public class OwnItemException extends ApiException {
    public OwnItemException() {
        super("Невозможно для собственных вещей", HttpStatus.CONFLICT);
    }
}
