package ru.practicum.shareit.item.exception;

public class ItemNameIsEmptyException extends RuntimeException {
    public ItemNameIsEmptyException(String message) {
        super(message);
    }
}
