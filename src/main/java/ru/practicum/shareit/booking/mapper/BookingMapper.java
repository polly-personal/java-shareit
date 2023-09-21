package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);
}
