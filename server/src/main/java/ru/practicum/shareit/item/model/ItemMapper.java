package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.model.dto.ItemRequest;
import ru.practicum.shareit.item.model.dto.ItemResponse;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDates;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    @Mapping(source = "requestId",
            target = "request.id",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item toItem(ItemRequest request);

    @Mapping(target = "requestId", source = "request.id")
    ItemResponse toResponse(Item item);

    ItemWithBookingDates toResponseWithDates(Item item);
}