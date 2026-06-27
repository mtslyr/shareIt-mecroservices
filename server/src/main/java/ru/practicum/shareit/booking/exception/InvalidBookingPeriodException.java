package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exception.ApiException;

public class InvalidBookingPeriodException extends ApiException {
    public InvalidBookingPeriodException() {
        super("Невозможно создать бранирование в указанные даты", HttpStatus.BAD_REQUEST);
    }
}
