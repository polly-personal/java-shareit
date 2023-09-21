package ru.practicum.shareit.user.exception;

public class UserErrorIdNotFoundByItemIdException extends RuntimeException {
    public UserErrorIdNotFoundByItemIdException(String message) {
        super(message);
    }
}
