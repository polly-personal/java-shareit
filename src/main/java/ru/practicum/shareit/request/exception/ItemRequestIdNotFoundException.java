package ru.practicum.shareit.request.exception;

public class ItemRequestIdNotFoundException extends RuntimeException {
    public ItemRequestIdNotFoundException(String message) {
        super(message);
    }
}
