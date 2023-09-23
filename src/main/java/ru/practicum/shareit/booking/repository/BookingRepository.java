package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.CustomerReview;

public interface BookingRepository {
    Booking create(Booking booking);

    Booking approve(Long bookingId);

    Booking reject(Long bookingId);

    Booking cancel(Long bookingId);

    Booking getById(Long bookingId);

    Booking createCustomerReview(Booking booking, CustomerReview customerReview);

    boolean idIsExists(Long id);

    boolean bookingIsExists(Booking booking);
}
