package ru.practicum.shareit.booking.exception;

public class BookingStatusAlreadyApproved extends RuntimeException {
    public BookingStatusAlreadyApproved(String message) {
        super(message);
    }
}
