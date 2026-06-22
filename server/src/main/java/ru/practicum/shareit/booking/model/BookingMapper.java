package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.model.dto.BookingRequest;
import ru.practicum.shareit.booking.model.dto.BookingResponse;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {

    Booking toBooking(BookingRequest request);

    @Mapping(source = "item", target = "item")
    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    @Mapping(source = "booker", target = "booker")
    BookingResponse toResponse(Booking booking);

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

    default Status map(Status status) {
        return status;
    }
}