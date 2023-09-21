package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingIdNotFoundException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice(basePackageClasses = BookingController.class)
public class BookingErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookingErrorResponse handelBookingIdNotFoundException(BookingIdNotFoundException e) {
        log.warn("🟥📗 404 - Not found: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookingErrorResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("🟥📗 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new BookingErrorResponse("🟥📗 некорректный json: " + e.getMessage());
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
