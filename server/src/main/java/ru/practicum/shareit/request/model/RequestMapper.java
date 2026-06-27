package ru.practicum.shareit.request.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.model.dto.RequestDTO;
import ru.practicum.shareit.request.model.dto.RequestResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ItemMapper.class)
public interface RequestMapper {

    Request toRequest(RequestDTO dto);

    @Mapping(target = "items", source = "items")
    RequestResponse toResponse(Request request);
}
