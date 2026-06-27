package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.model.dto.CommentRequest;
import ru.practicum.shareit.item.comment.model.dto.CommentResponse;

public interface CommentService {
    CommentResponse addComment(Long userId, Long itemId, CommentRequest commentRequest);
}
