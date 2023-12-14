package ru.practicum.shareit.request.controller;

public class ItemRequestErrorResponse {
    private String error;

    public ItemRequestErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
