package ru.practicum.shareit.user.exception;

public class UserIdNotFound extends RuntimeException {
    public UserIdNotFound(String message) {
        super(message);
    }
}
