package ru.practicum.shareit.booking.exception;

public class BookingRequestorIdNotLinkedToBookerIdOrOwnerId extends RuntimeException {
    public BookingRequestorIdNotLinkedToBookerIdOrOwnerId(String message) {
        super(message);
    }
}
