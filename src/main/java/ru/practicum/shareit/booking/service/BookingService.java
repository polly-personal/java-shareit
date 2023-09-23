package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto getById(Long bookingId);

    BookingDto create(Long userId, BookingDto bookingDto);

    BookingDto approve(Long ownerId, Long bookingId);

    BookingDto reject(Long ownerId, Long bookingId);

    BookingDto cancel(Long userId, Long bookingId);

    void idIsExists(Long id);

    BookingDto createCustomerReview(Long bookingId, CustomerReview customerReview);
}
