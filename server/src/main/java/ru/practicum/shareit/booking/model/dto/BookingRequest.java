package ru.practicum.shareit.booking.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BookingRequest {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
