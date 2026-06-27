package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.ItemRequest;
import ru.practicum.shareit.item.model.dto.ItemResponse;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDates;

import java.util.List;

public interface ItemService {

    List<ItemResponse> getAll();

    List<ItemResponse> searchItems(String text);

    List<ItemWithBookingDates> getAllItemsByUserId(Long userId);

    ItemWithBookingDates getItemById(Long itemId, Long userId);

    ItemResponse save(Long userId, ItemRequest request);

    ItemResponse update(Long userId, Long itemId, ItemRequest request);

    void delete(Long userId, Long itemId);
}
