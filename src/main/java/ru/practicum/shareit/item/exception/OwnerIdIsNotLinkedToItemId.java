package ru.practicum.shareit.item.exception;

public class OwnerIdIsNotLinkedToItemId extends RuntimeException {
    public OwnerIdIsNotLinkedToItemId(String message) {
        super(message);
    }
}
