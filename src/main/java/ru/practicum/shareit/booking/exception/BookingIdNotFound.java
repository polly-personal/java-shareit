package ru.practicum.shareit.booking.exception;

public class BookingIdNotFound extends RuntimeException {
    public BookingIdNotFound(String message) {
        super(message);
    }
}
