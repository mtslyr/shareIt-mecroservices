package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.exception.RequestNotFountException;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.request.model.dto.RequestDTO;
import ru.practicum.shareit.request.model.dto.RequestResponse;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final RequestMapper mapper;
    private final UserStorage userStorage;

    @Override
    @Transactional
    public RequestResponse createRequest(Long userId, RequestDTO request) {
        User requestor = userStorage.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        UserNotFoundException.CriteriaField.ID,
                        userId.toString()
                ));

        Request toSave = mapper.toRequest(request);
        toSave.setRequestor(requestor);
        toSave.setCreated(LocalDateTime.now());

        Request saved = repository.save(toSave);
        return mapper.toResponse(saved);
    }

    @Override
    public List<RequestResponse> getUserRequests(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new UserNotFoundException(UserNotFoundException.CriteriaField.ID, userId.toString());
        }

        List<Request> requests = repository.findByRequestorId(userId);

        return requests.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<RequestResponse> getAllRequests(Long userId) {
        List<Request> requests = repository.findByRequestorIdNot(userId);

        return requests.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RequestResponse getRequestById(Long requestId, Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new UserNotFoundException(UserNotFoundException.CriteriaField.ID, userId.toString());
        }

        Request request = repository.findById(requestId)
                .orElseThrow(() -> new RequestNotFountException(requestId));

        return mapper.toResponse(request);
    }
}