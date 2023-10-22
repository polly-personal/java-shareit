package ru.practicum.shareit.booking.exception;

public class BookingIdNotFoundException extends RuntimeException {
    public BookingIdNotFoundException(String message) {
        super(message);
    }
}
