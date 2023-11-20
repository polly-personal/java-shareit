package ru.practicum.shareit.item.exception;

public class ItemDescriptionIsEmpty extends RuntimeException {
    public ItemDescriptionIsEmpty(String message) {
        super(message);
    }
}
