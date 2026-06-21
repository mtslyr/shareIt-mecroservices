package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
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

        if (!(booker.getId().equals(userId) || item.getUserId().equals(userId))) {
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
                result = bookingStorage.findCurrentBookingsByUserId(userId, Instant.now());
            }
            case FUTURE -> {
                result = bookingStorage.findFutureBookingsByUserId(userId, Instant.now());
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

        LocalDateTime requestStart = request.getStartDate()
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();

        LocalDateTime requestEnd = request.getEndDate()
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();

        LocalDateTime now = Instant.now()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (requestStart.equals(requestEnd)
            || requestEnd.isBefore(requestStart)
            || (requestStart.isBefore(now) && requestEnd.isBefore(now))) {
            throw new InvalidBookingPeriodException();
        }

        if (item.getUserId().equals(userId)) {
            throw new OwnItemException();
        }

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException(item.getId());
        }

        Booking booking = mapper.toBooking(request);
        booking.setCreatedAt(Instant.now());
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
                result = bookingStorage.findAllByItemUserIdAndEndDateBeforeOrderByCreatedAtAsc(
                        itemOwnerId, Instant.now());
            }
            case CURRENT -> {
                result = bookingStorage.findCurrentBookingsByItemOwner(itemOwnerId, Instant.now());
            }
            case FUTURE -> {
                result = bookingStorage.findFutureBookingsByItemOwner(itemOwnerId, Instant.now());
            }
            case WAITING -> {
                result = bookingStorage.findAllByItemUserIdAndStatusOrderByCreatedAtAsc(
                        itemOwnerId, Status.WAITING);
            }
            case REJECTED -> {
                result = bookingStorage.findAllByItemUserIdAndStatusOrderByCreatedAtAsc(
                        itemOwnerId, Status.REJECTED);
            }
            default -> throw new IllegalArgumentException("Необрабатываемый статус: " + state.name());
        }

        return result.stream()
                .map(mapper::toResponse)
                .toList();
    }

    private List<BookingResponse> getAllForOwner(Long userId) {
        return bookingStorage.findByItemUserIdEquals(userId)
                .stream().map(mapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse patchBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        Long itemOfBookingId = booking.getItem().getId();

        if (!itemService.isOwner(userId, itemOfBookingId)) {
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
