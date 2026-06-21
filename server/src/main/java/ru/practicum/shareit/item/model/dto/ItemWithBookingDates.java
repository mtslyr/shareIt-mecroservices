package ru.practicum.shareit.item.model.dto;

import lombok.Data;
import ru.practicum.shareit.item.comment.model.dto.CommentResponse;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemWithBookingDates {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Integer shareCount;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Collection<CommentResponse> comments;
}
