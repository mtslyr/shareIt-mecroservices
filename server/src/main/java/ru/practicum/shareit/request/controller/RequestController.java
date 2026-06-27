package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.RequestDTO;
import ru.practicum.shareit.request.model.dto.RequestResponse;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final RequestService requestService;

    @PostMapping
    public RequestResponse createRequest(
            @RequestHeader(value = X_SHARER_USER_ID) Long userId,
            @RequestBody RequestDTO requestDTO) {
        return requestService.createRequest(userId, requestDTO);
    }

    @GetMapping
    public List<RequestResponse> getUserRequests(
            @RequestHeader(value = X_SHARER_USER_ID) Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestResponse> getAllRequests(
            @RequestHeader(value = X_SHARER_USER_ID) Long userId) {
        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public RequestResponse getRequestById(
            @RequestHeader(value = X_SHARER_USER_ID) Long userId,
            @PathVariable("requestId") Long requestId) {
        return requestService.getRequestById(requestId, userId);
    }
}