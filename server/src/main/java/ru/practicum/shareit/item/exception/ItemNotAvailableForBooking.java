package ru.practicum.shareit.item.exception;

public class ItemNotAvailableForBooking extends RuntimeException {
    public ItemNotAvailableForBooking(String message) {
        super(message);
    }
}
