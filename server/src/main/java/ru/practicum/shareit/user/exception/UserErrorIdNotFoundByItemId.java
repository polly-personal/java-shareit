package ru.practicum.shareit.user.exception;

public class UserErrorIdNotFoundByItemId extends RuntimeException {
    public UserErrorIdNotFoundByItemId(String message) {
        super(message);
    }
}
