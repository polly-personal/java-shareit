package ru.practicum.shareit.item.exception;

public class NoItemsExistsYet extends RuntimeException {
    public NoItemsExistsYet(String message) {
        super(message);
    }
}
