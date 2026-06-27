package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.common.validation.OnUpdate;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.model.dto.ItemRequest;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long id) {
        return itemClient.getItem(userId, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.searchItems(text);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestBody @Validated(OnCreate.class) ItemRequest request) {
        return itemClient.create(userId, request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchItem(
            @PathVariable Long id,
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestBody @Validated(OnUpdate.class) ItemRequest request) {
        return itemClient.update(userId, id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        itemClient.delete(userId, id);
    }
}