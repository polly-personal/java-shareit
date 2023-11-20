package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(long userId, BookingDtoIn bookingDtoIn);

    BookingDtoOut updateStatus(long ownerId, long bookingId, boolean isApproval);

    BookingDtoOut getById(long bookingId);

    BookingDtoOut getByIdAndRequestorId(long bookerId, long bookingId);

    List<BookingDtoOut> getAllByBookerId(long bookerId, String state);

    List<BookingDtoOut> getAllByOwnerId(long ownerId, String state);
}
