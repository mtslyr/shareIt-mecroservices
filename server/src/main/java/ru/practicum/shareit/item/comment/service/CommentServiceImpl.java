package ru.practicum.shareit.item.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.comment.exception.CommentNotAvailable;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.model.dto.CommentRequest;
import ru.practicum.shareit.item.comment.model.dto.CommentResponse;
import ru.practicum.shareit.item.comment.repository.CommentStorage;
import ru.practicum.shareit.item.exception.AccessException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentMapper mapper;
    private final CommentStorage commentStorage;
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public CommentResponse addComment(Long userId, Long itemId, CommentRequest commentRequest) {
        Comment comment = mapper.toComment(commentRequest);

        comment.setUser(userStorage.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        UserNotFoundException.CriteriaField.ID,
                        userId.toString())));

        comment.setItem(itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId)));

        comment.setCreatedAt(Instant.now());

        validateComment(userId, itemId);

        Comment saved = commentStorage.save(comment);

        return mapper.toResponse(saved);
    }

    private void validateComment(Long userId, Long itemId) {
        Booking booking = bookingStorage.findByBookerIdAndItemId(userId, itemId)
                .orElseThrow(AccessException::new);

        if (!booking.getStatus().equals(Status.APPROVED)) {
            throw new AccessException();
        }

        LocalDateTime now = Instant.now()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime bookingEndDate = booking.getEndDate()
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();

        if (bookingEndDate.isAfter(now)) {
            throw new CommentNotAvailable();
        }
    }
}
