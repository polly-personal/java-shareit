package ru.practicum.shareit.request.exception;

public class ItemRequestIdNotFound extends RuntimeException {
    public ItemRequestIdNotFound(String message) {
        super(message);
    }
}
