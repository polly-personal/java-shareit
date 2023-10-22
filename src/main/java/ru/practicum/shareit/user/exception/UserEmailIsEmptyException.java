package ru.practicum.shareit.user.exception;

public class UserEmailIsEmptyException extends RuntimeException {
    public UserEmailIsEmptyException(String message) {
        super(message);
    }
}
