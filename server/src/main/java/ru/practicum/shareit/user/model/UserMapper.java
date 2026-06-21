package ru.practicum.shareit.user.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.user.model.dto.UserRequest;
import ru.practicum.shareit.user.model.dto.UserResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserRequest request);

    UserResponse toResponse(User user);
}
