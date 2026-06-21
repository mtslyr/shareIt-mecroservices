package ru.practicum.shareit.request.exception;

import ru.practicum.shareit.common.exception.NotFoundException;

public class RequestNotFountException extends NotFoundException {
    public RequestNotFountException(Long requestId) {
        super("Не найден запрос с ID = " + requestId);
    }
}
