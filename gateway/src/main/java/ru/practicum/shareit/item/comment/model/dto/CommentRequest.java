package ru.practicum.shareit.item.comment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.common.validation.OnCreate;

@Getter
@Setter
public class CommentRequest {
    @NotNull(groups = OnCreate.class, message = "Комментарий не может быть пустым")
    String text;
}
