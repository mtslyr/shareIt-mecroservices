package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(CriteriaField field, String value) {
        super("Пользователь с %s = %s не найден.".formatted(field.name(), value));
    }

    public enum CriteriaField {
        EMAIL, ID
    }
}
