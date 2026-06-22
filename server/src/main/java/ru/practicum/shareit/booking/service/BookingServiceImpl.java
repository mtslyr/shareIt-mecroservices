package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.InvalidBookingPeriodException;
import ru.practicum.shareit.booking.exception.ItemNotAvailableException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingRequest;
import ru.practicum.shareit.booking.model.dto.BookingResponse;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.common.exception.OwnItemException;
import ru.practicum.shareit.item.exception.AccessException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final ItemServiceImpl itemService;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final BookingMapper mapper;

    @Override
    public BookingResponse getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        Item item = booking.getItem();
        User booker = booking.getBooker();

        if (!(booker.getId().equals(userId) || item.getOwner().getId().equals(userId))) {
            throw new AccessException();
        }

        return mapper.toResponse(booking);
    }

    @Override
    public List<BookingResponse> getAll(Long userId) {
        return bookingStorage.findAllByBookerIdOrderByCreatedAtAsc(userId)
                .stream().map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getAllByState(Long userId, State state) {
        List<Booking> result;
        switch (state) {
            case ALL -> {
                return getAll(userId);
            }
            case PAST -> {
                result = bookingStorage.findAllByBookerIdAndEndDateBeforeOrderByCreatedAtAsc(
                        userId, Instant.now());
            }
            case CURRENT -> {
                result = bookingStorage.findCurrentBookingsByOwnerId(userId, Instant.now());
            }
            case FUTURE -> {
                result = bookingStorage.findFutureBookingsByOwnerId(userId, Instant.now());
            }
            case WAITING -> {
                result = bookingStorage.findAllByBookerIdAndStatusOrderByCreatedAtAsc(
                        userId, Status.WAITING);
            }
            case REJECTED -> {
                result = bookingStorage.findAllByBookerIdAndStatusOrderByCreatedAtAsc(
                        userId, Status.REJECTED);
            }
            default -> throw new IllegalArgumentException("Необрабатываемый статус: " + state.name());
        }

        return result.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse create(Long userId, BookingRequest request) {
        validateUserExists(userId);

        User booker = userStorage.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                UserNotFoundException.CriteriaField.ID,
                                userId.toString()));

        Item item = itemStorage.findById(request.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(request.getItemId()));

        Booking booking = mapper.toBooking(request);

        Instant nowInstant = Instant.now();
        Instant startInstant = booking.getStartDate();
        Instant endInstant = booking.getEndDate();

        log.info("Даты бронирования: start - {}", startInstant);
        log.info("Даты бронирования: end - {}", endInstant);

        if (startInstant.equals(endInstant) || endInstant.isBefore(startInstant)) {
            throw new InvalidBookingPeriodException();
        }

        if (startInstant.isBefore(nowInstant.minus(java.time.Duration.ofHours(12)))) {
            throw new InvalidBookingPeriodException();
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new OwnItemException();
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException(item.getId());
        }

        booking.setCreatedAt(nowInstant);
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        Booking saved = bookingStorage.save(booking);
        return mapper.toResponse(saved);
    }

    @Override
    public List<BookingResponse> getBookingsForOwner(Long itemOwnerId, State state) {
        validateUserExists(itemOwnerId);

        List<Booking> result;
        switch (state) {
            case ALL -> {
                return getAllForOwner(itemOwnerId);
            }
            case PAST -> {
                result = bookingStorage.findAllByItemOwnerIdAndEndDateBeforeOrderByCreatedAtAsc(
                        itemOwnerId, Instant.now());
            }
            case CURRENT -> {
                result = bookingStorage.findCurrentBookingsByItemOwner(itemOwnerId, Instant.now());
            }
            case FUTURE -> {
                result = bookingStorage.findFutureBookingsByItemOwner(itemOwnerId, Instant.now());
            }
            case WAITING -> {
                result = bookingStorage.findAllByItemOwnerIdAndStatusOrderByCreatedAtAsc(
                        itemOwnerId, Status.WAITING);
            }
            case REJECTED -> {
                result = bookingStorage.findAllByItemOwnerIdAndStatusOrderByCreatedAtAsc(
                        itemOwnerId, Status.REJECTED);
            }
            default -> throw new IllegalArgumentException("Необрабатываемый статус: " + state.name());
        }

        return result.stream()
                .map(mapper::toResponse)
                .toList();
    }

    private List<BookingResponse> getAllForOwner(Long userId) {
        return bookingStorage.findByItemOwnerIdEquals(userId)
                .stream().map(mapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse patchBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        Item itemOfBooking = booking.getItem();

        if (!itemOfBooking.getOwner().getId().equals(userId)) {
            throw new AccessException();
        }

        Status targetStatus = approved ? Status.APPROVED : Status.REJECTED;

        if (booking.getStatus().equals(targetStatus)) {
            return mapper.toResponse(booking);
        }

        booking.setStatus(targetStatus);
        Booking saved = bookingStorage.save(booking);

        return mapper.toResponse(saved);
    }

    private void validateUserExists(Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        UserNotFoundException.CriteriaField.ID,
                        userId.toString()));
    }
}
