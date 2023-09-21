package ru.practicum.shareit.item.exception;

public class ItemDescriptionIsEmptyException extends RuntimeException {
    public ItemDescriptionIsEmptyException(String message) {
        super(message);
    }
}
