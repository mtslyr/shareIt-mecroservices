package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.model.dto.BookingRequest;
import ru.practicum.shareit.common.validation.OnCreate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Validated
@Slf4j
public class BookingController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForOwner(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getBookingsForOwner(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> postBooking(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestBody @Validated(OnCreate.class) BookingRequest bookingRequest) {
        log.debug("Creating booking: {}", bookingRequest);
        return bookingClient.create(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchBooking(
            @PathVariable Long bookingId,
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam Boolean approved) {
        return bookingClient.updateBooking(bookingId, userId, approved);
    }
}