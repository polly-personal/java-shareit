package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingRepository {
    BookingDto create(Booking booking);

    BookingDto approve(Long bookingId);

    BookingDto reject(Long bookingId);

    BookingDto cancel(Long bookingId);

    BookingDto getById(Long bookingId);

    BookingDto createCustomerReview(BookingDto bookingDto, CustomerReview customerReview);

    void idIsExists(Long id);
}
