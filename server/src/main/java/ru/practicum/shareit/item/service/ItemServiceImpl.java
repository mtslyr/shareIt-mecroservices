package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.model.dto.CommentResponse;
import ru.practicum.shareit.item.comment.repository.CommentStorage;
import ru.practicum.shareit.item.exception.AccessException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingProjection;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.dto.ItemRequest;
import ru.practicum.shareit.item.model.dto.ItemResponse;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDates;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.request.exception.RequestNotFountException;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper mapper;
    private final ItemStorage repository;
    private final UserStorage userRepository;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final CommentMapper commentMapper;
    private final RequestRepository requestRepository;

    @Override
    public List<ItemResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<ItemResponse> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return repository.search(text)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<ItemWithBookingDates> getAllItemsByUserId(Long userId) {
        Map<Long, ItemWithBookingDates> userItems = repository.findAllByOwnerId(userId)
                .stream()
                .map(mapper::toResponseWithDates)
                .collect(Collectors.toMap(
                        ItemWithBookingDates::getId,
                        Function.identity()
                ));

        Map<Long,List<CommentResponse>> itemsComments = commentStorage
                .findByItemIdIn(userItems.keySet())
                .stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(commentMapper::toResponse, Collectors.toList())
                ));

        bookingStorage.findAllByItemsWithBookingDates(userId)
                .forEach(projection -> {
                    ItemWithBookingDates item = userItems.get(projection.getItemId());

                    if (item != null) {
                        item.setNextBooking(projection.getNextBookingStart());
                        item.setLastBooking(projection.getLastBookingEnd());
                        item.setComments(itemsComments.getOrDefault(
                                projection.getItemId(),
                                List.of()
                        ));
                    }
                });

        return new ArrayList<>(userItems.values());
    }

    @Override
    public ItemWithBookingDates getItemById(Long itemId) {
        ItemBookingProjection projection = bookingStorage.findItemWithBookingDates(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        Item item = repository.findById(projection.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        ItemWithBookingDates itemWithBookingDates = mapper.toResponseWithDates(item);

        itemWithBookingDates.setNextBooking(projection.getNextBookingStart());
        itemWithBookingDates.setLastBooking(projection.getLastBookingEnd());

        itemWithBookingDates.setComments(getCommentsForItem(item.getId()));

        return itemWithBookingDates;
    }

    @Override
    public ItemResponse save(Long userId, ItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                UserNotFoundException.CriteriaField.ID,
                                userId.toString()));

        if (request.getRequestId() != null) {
            requestRepository.findById(request.getRequestId())
                    .orElseThrow(() -> new RequestNotFountException(request.getRequestId()));
        }

        Item item = mapper.toItem(request);
        item.setOwner(user);

        if (request.getRequestId() == null) {
            item.setRequest(null);
        }

        return mapper.toResponse(
                repository.save(item));
    }

    @Override
    public ItemResponse update(Long userId, Long itemId, ItemRequest request) {
        if (!isOwner(userId, itemId)) {
            throw new AccessException();
        }

        Item i = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        patchItem(i, request);

        return mapper.toResponse(
                repository.save(i));
    }

    @Override
    public void delete(Long userId, Long itemId) {
        if (!isOwner(userId, itemId)) {
            throw new AccessException();
        }

        repository.deleteById(itemId);
    }

    public boolean isOwner(Long userId, Long itemId) {
        return repository.findById(itemId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                UserNotFoundException.CriteriaField.ID,
                                userId.toString()))
                .getOwner().equals(userId);
    }

    private void patchItem(Item item, ItemRequest request) {
        if (request.getName() != null) {
            item.setName(request.getName());
        }

        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }

        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }
    }

    private List<CommentResponse> getCommentsForItem(Long itemId) {
        return commentStorage.findByItemId(itemId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}