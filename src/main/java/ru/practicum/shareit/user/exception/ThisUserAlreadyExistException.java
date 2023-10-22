package ru.practicum.shareit.user.exception;

public class ThisUserAlreadyExistException extends RuntimeException {
    public ThisUserAlreadyExistException(String message) {
        super(message);
    }
}
