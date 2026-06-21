package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.common.validation.OnUpdate;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.model.dto.UserRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        return userClient.get(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(OnCreate.class) UserRequest request) {
        return userClient.create(request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUser(
            @PathVariable long id,
            @RequestBody @Validated(OnUpdate.class) UserRequest request) {
        return userClient.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userClient.delete(id);
    }
}