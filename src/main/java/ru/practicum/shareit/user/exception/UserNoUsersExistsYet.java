package ru.practicum.shareit.user.exception;

public class UserNoUsersExistsYet extends RuntimeException {
    public UserNoUsersExistsYet(String message) {
        super(message);
    }
}
