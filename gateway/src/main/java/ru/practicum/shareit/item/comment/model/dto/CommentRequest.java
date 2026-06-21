package ru.practicum.shareit.item.comment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.common.validation.OnCreate;

@Data
public class CommentRequest {
    @NotNull(groups = OnCreate.class, message = "Комментарий не может быть пустым")
    String text;
}
