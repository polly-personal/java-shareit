package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.dto.GiveBookingDto;

public interface BookingService {
    GiveBookingDto getById(Long bookingId);

    GiveBookingDto create(Long userId, GiveBookingDto giveBookingDto);

    GiveBookingDto approve(Long ownerId, Long bookingId);

    GiveBookingDto reject(Long ownerId, Long bookingId);

    GiveBookingDto cancel(Long userId, Long bookingId);

    void idIsExists(Long id);

    GiveBookingDto createCustomerReview(Long bookingId, CustomerReview customerReview);
}
