package ru.practicum.shareit.user.exception;

public class UserEmailIsEmpty extends RuntimeException {
    public UserEmailIsEmpty(String message) {
        super(message);
    }
}
