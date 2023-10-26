package ru.practicum.shareit.item.exception;

public class ItemNoItemsExistsYet extends RuntimeException {
    public ItemNoItemsExistsYet(String message) {
        super(message);
    }
}
