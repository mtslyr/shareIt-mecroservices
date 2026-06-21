package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.common.validation.OnUpdate;
import ru.practicum.shareit.user.model.dto.UserRequest;
import ru.practicum.shareit.user.model.dto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Validated(OnCreate.class) UserRequest request) {
        return userService.save(request);
    }

    @PatchMapping("/{id}")
    public UserResponse patchUser(
            @PathVariable("id") long userId,
            @RequestBody @Validated(OnUpdate.class) UserRequest request) {
        return userService.update(userId,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long userId) {
        userService.delete(userId);
    }

}
