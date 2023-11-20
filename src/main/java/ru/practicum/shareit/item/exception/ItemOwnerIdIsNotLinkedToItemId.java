package ru.practicum.shareit.item.exception;

public class ItemOwnerIdIsNotLinkedToItemId extends RuntimeException {
    public ItemOwnerIdIsNotLinkedToItemId(String message) {
        super(message);
    }
}
