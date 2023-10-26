package ru.practicum.shareit.booking.exception;

public class BookingIncorrectDates extends RuntimeException {
    public BookingIncorrectDates(String message) {
        super(message);
    }
}
