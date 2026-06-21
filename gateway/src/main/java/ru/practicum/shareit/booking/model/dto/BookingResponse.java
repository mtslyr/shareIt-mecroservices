package ru.practicum.shareit.booking.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.dto.ItemResponse;
import ru.practicum.shareit.user.model.dto.UserResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    Long id;
    ItemResponse item;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    UserResponse booker;
}
