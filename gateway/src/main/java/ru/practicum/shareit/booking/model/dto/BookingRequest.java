package ru.practicum.shareit.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.serializing.DateDeserializer;
import ru.practicum.shareit.common.validation.OnCreate;
import ru.practicum.shareit.common.validation.OnUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    @NotNull(groups = OnCreate.class,
            message = "ID вещи должен быть указан")
    Long itemId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class},
            message = "Дата начала должна быть указана")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("start")
    LocalDateTime start;

    @NotNull(groups = {OnCreate.class, OnUpdate.class},
            message = "Дата окончания должна быть указана")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("end")
    LocalDateTime end;
}
