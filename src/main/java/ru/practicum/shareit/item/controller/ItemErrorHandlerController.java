package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.*;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice(basePackageClasses = ItemController.class)
public class ItemErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemErrorResponse handelItemIdNotFoundException(ItemIdNotFoundException e) {
        log.warn("🟥📦 404 - Not found: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemErrorResponse handelOwnerIdIsNotLinkedToItemId(OwnerIdIsNotLinkedToItemId e) {
        log.warn("🟥📦 404 - Not found: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemErrorResponse handelNoItemsExistsYet(NoItemsExistsYet e) {
        log.warn("🟥📦 404 - Not found: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 " + e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handelItemNameIsEmptyException(ItemNameIsEmptyException e) {
        log.warn("🟥📦 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handelItemDescriptionIsEmptyException(ItemDescriptionIsEmptyException e) {
        log.warn("🟥📦 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handelItemAvailableIsEmptyException(ItemAvailableIsEmptyException e) {
        log.warn("🟥📦 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("🟥📦 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 некорректный json: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handelMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.warn("🟥📦 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 некорректный заголовок или его значение: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handelConstraintViolationException(ConstraintViolationException e) {
        log.warn("🟥📦 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemErrorResponse("🟥📦 некорректная переменная пути: " + e.getMessage());
    }
}
