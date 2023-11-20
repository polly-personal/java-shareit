package ru.practicum.shareit.item.exception;

public class ItemAvailableIsEmpty extends RuntimeException {
    public ItemAvailableIsEmpty(String message) {
        super(message);
    }
}
