package ru.practicum.shareit.item.comment.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long id;
    Long userId;
    Long itemId;
    String text;
    String authorName;
    LocalDateTime created;
}
