package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.util.List;

public interface BookingService {
    BookingDtoOut getById(Long bookingId);

    BookingDtoOut getByIdAndRequestorId(Long bookerId, Long bookingId);

    List<BookingDtoOut> getAllByBookerId(Long bookerId, String state);

    List<BookingDtoOut> getAllByOwnerId(Long ownerId, String state);

    BookingDtoOut create(Long userId, BookingDtoIn bookingDtoIn);

    BookingDtoOut updateStatus(Long ownerId, Long bookingId, boolean isApproval);
}
