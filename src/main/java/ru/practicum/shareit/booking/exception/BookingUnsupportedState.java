package ru.practicum.shareit.booking.exception;

public class BookingUnsupportedState extends RuntimeException {
    public BookingUnsupportedState(String message) {
        super(message);
    }
}
