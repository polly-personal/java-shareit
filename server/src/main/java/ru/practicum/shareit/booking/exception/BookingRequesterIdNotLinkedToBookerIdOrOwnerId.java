package ru.practicum.shareit.booking.exception;

public class BookingRequesterIdNotLinkedToBookerIdOrOwnerId extends RuntimeException {
    public BookingRequesterIdNotLinkedToBookerIdOrOwnerId(String message) {
        super(message);
    }
}
