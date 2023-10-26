package ru.practicum.shareit.booking.exception;

public class BookingNotAvailable extends RuntimeException {
    public BookingNotAvailable(String message) {
        super(message);
    }
}
