package ru.practicum.shareit.item.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.comment.exception.CommentNotAvailable;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.model.dto.CommentRequest;
import ru.practicum.shareit.item.comment.model.dto.CommentResponse;
import ru.practicum.shareit.item.comment.repository.CommentStorage;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentMapper mapper;
    private final CommentStorage commentStorage;
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Transactional
    @Override
    public CommentResponse addComment(Long userId, Long itemId, CommentRequest request) {
        validateComment(userId, itemId);

        User author = userStorage.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        UserNotFoundException.CriteriaField.ID,
                        userId.toString()
                ));
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        Comment comment = mapper.toComment(request);
        comment.setUser(author);
        comment.setItem(item);

        comment.setCreatedAt(Instant.now());

        Comment savedComment = commentStorage.save(comment);
        return mapper.toResponse(savedComment);
    }

    private void validateComment(Long userId, Long itemId) {
        Booking bookingToComment = bookingStorage
                .findFirstByBookerIdAndItemIdAndStatusOrderByEndDesc(
                        userId,
                        itemId,
                        Status.APPROVED)
                .orElseThrow(() -> new CommentNotAvailable());

        log.info("Found booking for validation: {}", bookingToComment);

        Instant now = Instant.now();
        Instant end = bookingToComment.getEnd();

        log.info("Проверяем завершение бронирования: end={}, now={}", end, now);

        if (end.isAfter(now)) {
            log.info("Бронирование еще не завершено: end={}, now={}", end, now);
            throw new CommentNotAvailable();
        }
    }
}
