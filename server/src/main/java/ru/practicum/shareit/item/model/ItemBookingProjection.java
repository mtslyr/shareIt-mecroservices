package ru.practicum.shareit.item.model;

import java.time.LocalDateTime;

public interface ItemBookingProjection {

    Long getItemId();

    LocalDateTime getNextBookingStart();

    LocalDateTime getLastBookingEnd();
}