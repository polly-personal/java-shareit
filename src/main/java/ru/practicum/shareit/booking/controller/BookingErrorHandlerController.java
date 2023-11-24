package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingUnsupportedState;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.controller.ItemErrorResponse;
import ru.practicum.shareit.item.exception.ItemIdNotFound;
import ru.practicum.shareit.item.exception.ItemNotAvailableForBooking;
import ru.practicum.shareit.item.exception.ItemOwnerIdIsNotLinkedToItemId;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice(basePackageClasses = {BookingController.class/*, ItemServiceImpl.class*/})
public class BookingErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookingErrorResponse handelBookingIdNotFoundException(BookingIdNotFound e) {
        log.warn("🟥📗 404 - Not found: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookingErrorResponse handelBookingRequesterIdNotLinkedToBookerIdOrOwnerId(BookingRequesterIdNotLinkedToBookerIdOrOwnerId e) {
        log.warn("🟥📗 404 - Not found: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookingErrorResponse handelBookingBookerIdIsOwnerId(BookingBookerIdIsOwnerId e) {
        log.warn("🟥📗 404 - Not found: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookingErrorResponse handelOwnerIdIsNotLinkedToItemId(ItemOwnerIdIsNotLinkedToItemId e) {
        log.warn("🟥📦 404 - Not found: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📦 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookingErrorResponse handelItemIdNotFound(ItemIdNotFound e) {
        log.warn("🟥📦 404 - Not found: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📦 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 некорректный json: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelBookingIncorrectDatesException(BookingIncorrectDates e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 некорректный json: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelBookingNotAvailableException(BookingNotAvailable e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 некорректный json: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelBookingStatusAlreadyApproved(BookingStatusAlreadyApproved e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 некорректный запрос: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelBookingUnsupportedState(BookingUnsupportedState e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handelItemNotAvailableForBooking(ItemNotAvailableForBooking e) {
        log.warn("🟥📦 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 " + e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 некорректный заголовок или его значение: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelConstraintViolationException(ConstraintViolationException e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 некорректная переменная пути: " + e.getMessage());
    }
}
