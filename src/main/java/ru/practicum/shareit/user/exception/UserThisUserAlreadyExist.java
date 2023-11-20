package ru.practicum.shareit.user.exception;

public class UserThisUserAlreadyExist extends RuntimeException {
    public UserThisUserAlreadyExist(String message) {
        super(message);
    }
}
