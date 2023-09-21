package ru.practicum.shareit.item.controller;

public class ItemErrorResponse {
    private String error;

    public ItemErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
