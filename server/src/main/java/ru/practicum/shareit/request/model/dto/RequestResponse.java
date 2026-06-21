package ru.practicum.shareit.request.model.dto;

import lombok.*;
import ru.practicum.shareit.item.model.dto.ItemResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponse {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponse> items = new ArrayList<>();
}