package ru.practicum.shareit.item.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.validation.OnCreate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {

    @NotBlank(groups = OnCreate.class, message = "Наименование должно быть указано")
    String name;

    @NotBlank(groups = OnCreate.class, message = "Описание должно быть указано")
    String description;

    @NotNull(groups = OnCreate.class, message = "Доступность должна быть указана")
    Boolean available;

    Long requestId;
}