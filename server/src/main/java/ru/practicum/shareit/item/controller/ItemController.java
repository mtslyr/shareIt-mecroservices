package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.common.validation.OnUpdate;
import ru.practicum.shareit.item.model.dto.ItemRequest;
import ru.practicum.shareit.item.model.dto.ItemResponse;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDates;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemWithBookingDates> getAll(
            @RequestHeader(value = X_SHARER_USER_ID) Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ItemWithBookingDates getItemById(
            @RequestHeader(value = X_SHARER_USER_ID) Long uerId,
            @PathVariable("id") Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemResponse> searchItems(
            @RequestHeader(value = X_SHARER_USER_ID) Long uerId,
            @RequestParam("text") String text) {
        return itemService.searchItems(text);
    }

    @PostMapping
    public ItemResponse createItem(
            @RequestHeader(value = X_SHARER_USER_ID) Long userId,
            @RequestBody @Validated(OnCreate.class) ItemRequest request) {
        return itemService.save(userId, request);
    }

    @PatchMapping("/{id}")
    public ItemResponse patchItem(
            @PathVariable("id") Long itemId,
            @RequestHeader(value = X_SHARER_USER_ID) Long userId,
            @RequestBody @Validated(OnUpdate.class) ItemRequest request) {
        return itemService.update(userId, itemId, request);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(
            @PathVariable("id") Long itemId,
            @RequestHeader(value = X_SHARER_USER_ID) Long userId) {
        itemService.delete(userId, itemId);
    }
}
