package ru.practicum.shareit.user.exception;

public class NoUsersExistsYet extends RuntimeException {
    public NoUsersExistsYet(String message) {
        super(message);
    }
}
