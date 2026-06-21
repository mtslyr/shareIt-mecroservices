package ru.practicum.shareit.item.model.dto;

import lombok.Data;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Integer shareCount;
    private Long requestId;
}