package ru.practicum.shareit.item.exception;

public class ItemNameIsEmpty extends RuntimeException {
    public ItemNameIsEmpty(String message) {
        super(message);
    }
}
