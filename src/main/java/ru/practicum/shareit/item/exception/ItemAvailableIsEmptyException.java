package ru.practicum.shareit.item.exception;

public class ItemAvailableIsEmptyException extends RuntimeException {
    public ItemAvailableIsEmptyException(String message) {
        super(message);
    }
}
