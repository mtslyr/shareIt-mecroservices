package ru.practicum.shareit.item.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.item.comment.model.dto.CommentRequest;
import ru.practicum.shareit.item.comment.model.dto.CommentResponse;
import ru.practicum.shareit.item.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final CommentService commentService;

    @PostMapping("/{itemId}/comment")
    public CommentResponse postComment(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable("itemId") Long itemId,
            @RequestBody @Validated(OnCreate.class) CommentRequest comment) {
        return commentService.addComment(userId, itemId, comment);
    }
}
