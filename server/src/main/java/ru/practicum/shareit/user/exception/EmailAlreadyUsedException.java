package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exception.ApiException;

public class EmailAlreadyUsedException extends ApiException {
    public EmailAlreadyUsedException(String email) {
        super("Пользователь с email '%s' уже существует".formatted(email), HttpStatus.CONFLICT);
    }
}
