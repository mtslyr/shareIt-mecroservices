package ru.practicum.shareit.request.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemAnswerDTO {
    private Long id;
    private String name;
    private Long ownerId;
}