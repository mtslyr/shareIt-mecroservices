package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.model.dto.BookingRequest;
import ru.practicum.shareit.booking.model.dto.BookingResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.validation.OnCreate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Slf4j
public class BookingController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingResponse getBookingById(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable("bookingId") Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponse> getBookings(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") State state) {
        return bookingService.getAllByState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getBookingsForOwner(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") State state) {
        return bookingService.getBookingsForOwner(userId, state);
    }

    @PostMapping
    public BookingResponse postBooking(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestBody @Validated(OnCreate.class) BookingRequest bookingRequest) {
        log.debug("Заявка на создание бронирования: {}", bookingRequest);
     return bookingService.create(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse patchBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam("approved") Boolean approved) {
        return bookingService.patchBooking(bookingId, userId, approved);
    }
}
