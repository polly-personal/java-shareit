package ru.practicum.shareit.user.controller;

public class UserErrorResponse {
    private String error;

    public UserErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
