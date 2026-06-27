package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.dto.RequestDTO;
import ru.practicum.shareit.request.model.dto.RequestResponse;

import java.util.List;

public interface RequestService {

    RequestResponse createRequest(Long userId, RequestDTO request);

    List<RequestResponse> getUserRequests(Long userId);

    List<RequestResponse> getAllRequests(Long userId);

    RequestResponse getRequestById(Long requestId, Long userId);
}
