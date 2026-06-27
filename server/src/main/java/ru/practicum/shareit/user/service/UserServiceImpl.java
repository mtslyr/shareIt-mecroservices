package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.EmailAlreadyUsedException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.model.dto.UserRequest;
import ru.practicum.shareit.user.model.dto.UserResponse;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userRepository;
    private final UserMapper mapper;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        return mapper.toResponse(userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                UserNotFoundException.CriteriaField.ID,
                                userId.toString())));
    }

    @Override
    public UserResponse save(UserRequest request) {
        validateUserEmail(request, null);
        return mapper.toResponse(
                userRepository.save(mapper.toUser(request)));
    }

    @Override
    public UserResponse update(Long userId, UserRequest request) {
        User u = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                UserNotFoundException.CriteriaField.ID,
                                userId.toString()));


        if (request.getEmail() != null) {
            validateUserEmail(request, userId);
        }


        patchUser(u, request);
        return mapper.toResponse(userRepository.save(u));
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public void validateUserEmail(UserRequest request, Long userId) {
        userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(userId)) {
                throw new EmailAlreadyUsedException(request.getEmail());
            }
        });
    }

    private void patchUser(User user, UserRequest request) {
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
    }
}
