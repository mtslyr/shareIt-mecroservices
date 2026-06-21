package ru.practicum.shareit.request.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.common.validation.OnCreate;

@Data
public class RequestDTO {

    @NotBlank(groups = OnCreate.class,
            message = "Описание должно быть указано и не быть пустым")
    @Size(groups = OnCreate.class,
            min = 5,
            max = 1024,
            message = "Описание должно быть от 5 до 1024 символов")
    private String description;
}