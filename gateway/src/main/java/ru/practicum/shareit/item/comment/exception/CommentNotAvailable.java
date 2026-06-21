package ru.practicum.shareit.item.comment.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exception.ApiException;

public class CommentNotAvailable extends ApiException {
    public CommentNotAvailable() {
        super("Невозможно оставить комментарий", HttpStatus.BAD_REQUEST);
    }
}
