package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.common.exception.NotFoundException;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(Long bookingId) {
        super("Бронирование с ID = %d не найдено".formatted(bookingId));
    }
}
