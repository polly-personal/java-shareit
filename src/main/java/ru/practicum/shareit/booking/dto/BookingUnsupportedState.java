package ru.practicum.shareit.booking.dto;

public class BookingUnsupportedState extends RuntimeException {
    public BookingUnsupportedState(String message) {
        super(message);
    }
}
