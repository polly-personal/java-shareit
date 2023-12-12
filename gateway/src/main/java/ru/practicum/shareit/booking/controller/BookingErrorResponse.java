package ru.practicum.shareit.booking.controller;

public class BookingErrorResponse {
    private String error;

    public BookingErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
