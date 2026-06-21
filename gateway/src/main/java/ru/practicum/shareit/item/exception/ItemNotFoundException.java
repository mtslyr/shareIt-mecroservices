package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.common.exception.NotFoundException;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(Long id) {
        super("Вещь с ID = %d не найден.".formatted(id));
    }
}
