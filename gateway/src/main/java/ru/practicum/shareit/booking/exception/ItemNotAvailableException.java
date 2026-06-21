package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exception.ApiException;

public class ItemNotAvailableException extends ApiException {
    public ItemNotAvailableException(Long itemId) {
        super("Вещь %d недоступна в данный момент".formatted(itemId), HttpStatus.BAD_REQUEST);
    }
}
