package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.model.dto.BookingRequest;
import ru.practicum.shareit.booking.model.dto.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse getBookingById(Long userId, Long bookingId);

    List<BookingResponse> getAll(Long userId);

    List<BookingResponse> getAllByState(Long userId, State state);

    BookingResponse create(Long userId, BookingRequest request);

    BookingResponse patchBooking(Long bookingId, Long userId, Boolean approved);

    List<BookingResponse> getBookingsForOwner(Long userId, State state);
}
