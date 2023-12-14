package ru.practicum.shareit.booking.exception;

public class BookingBookerIdIsOwnerId extends RuntimeException {
    public BookingBookerIdIsOwnerId(String message) {
        super(message);
    }
}
