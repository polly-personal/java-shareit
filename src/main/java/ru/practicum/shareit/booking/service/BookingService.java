package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto create(Long userId, Booking booking);

    BookingDto approve(Long ownerId, Long bookingId);

    BookingDto reject(Long ownerId, Long bookingId);

    BookingDto cancel(Long userId, Long bookingId);

    BookingDto createCustomerReview(Long userId, Long bookingId, CustomerReview customerReview);
}
