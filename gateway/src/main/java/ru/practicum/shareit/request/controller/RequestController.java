package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.model.dto.RequestDTO;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestBody @Validated(OnCreate.class) RequestDTO requestDTO) {
        return requestClient.create(userId, requestDTO);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable Long requestId) {
        return requestClient.getRequest(userId, requestId);
    }
}