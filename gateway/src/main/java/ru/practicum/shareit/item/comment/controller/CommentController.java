package ru.practicum.shareit.item.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.item.comment.client.CommentClient;
import ru.practicum.shareit.item.comment.model.dto.CommentRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final CommentClient commentClient;

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable Long itemId,
            @RequestBody @Validated(OnCreate.class) CommentRequest comment) {
        return commentClient.addComment(userId, itemId, comment);
    }
}