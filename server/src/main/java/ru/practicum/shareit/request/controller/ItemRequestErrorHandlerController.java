package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.request.exception.ItemRequestIdNotFound;

@Slf4j
@RestControllerAdvice(basePackageClasses = ItemRequestController.class)
public class ItemRequestErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemRequestErrorResponse handelItemRequestIdNotFoundException(ItemRequestIdNotFound e) {
        log.warn("🟥🖍️ 404 - Not found: \"{}\"", e.getMessage(), e);
        return new ItemRequestErrorResponse("🟥🖍️ " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemRequestErrorResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("🟥🖍️ 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemRequestErrorResponse("🟥🖍️ некорректный json: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemRequestErrorResponse handelMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.warn("🟥🖍️ 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new ItemRequestErrorResponse("🟥🖍️ некорректный заголовок или его значение: " + e.getMessage());
    }
}
