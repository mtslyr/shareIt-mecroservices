package ru.practicum.shareit.item.comment.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.comment.model.dto.CommentRequest;
import ru.practicum.shareit.item.comment.model.dto.CommentResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.name", target = "authorName")
    @Mapping(source = "createdAt", target = "created")
    CommentResponse toResponse(Comment comment);

    @Mapping(source = "text", target = "text")
    Comment toComment(CommentRequest request);

    default Instant map(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.atZone(ZoneId.systemDefault()).toInstant();
    }

    default LocalDateTime map(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}