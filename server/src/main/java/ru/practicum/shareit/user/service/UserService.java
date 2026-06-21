package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.UserRequest;
import ru.practicum.shareit.user.model.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();

    UserResponse getUserById(Long userId);

    UserResponse save(UserRequest request);

    UserResponse update(Long userId, UserRequest request);

    void delete(Long userId);
}
