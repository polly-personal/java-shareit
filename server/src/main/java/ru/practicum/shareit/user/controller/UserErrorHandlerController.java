package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.exception.*;

@Slf4j
@RestControllerAdvice/*(basePackageClasses = {UserController.class, ItemController.class})*/
public class UserErrorHandlerController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorResponse handelUserIdNotFoundException(UserIdNotFound e) {
        log.warn("🟥 404 - Not found: \"{}\"", e.getMessage(), e);
        return new UserErrorResponse("🟥 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorResponse handelUserErrorIdNotFoundByItemIdException(UserErrorIdNotFoundByItemId e) {
        log.warn("🟥 404 - Not found: \"{}\"", e.getMessage(), e);
        return new UserErrorResponse("🟥 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorResponse handelNoUsersExistsYet(UserNoUsersExistsYet e) {
        log.warn("🟥 404 - Not found: \"{}\"", e.getMessage(), e);
        return new UserErrorResponse("🟥 " + e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorResponse handelUserEmailIsEmptyException(UserEmailIsEmpty e) {
        log.warn("🟥 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new UserErrorResponse("🟥 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public UserErrorResponse handelUserEmailAlreadyExistsException(UserEmailAlreadyExists e) {
        log.warn("🟥 409 - Conflict: \"{}\"", e.getMessage(), e);
        return new UserErrorResponse("🟥 " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("🟥 400 - Bad Request: \"{}\"", e.getMessage(), e);
        return new UserErrorResponse("🟥 некорректный json: " + e.getMessage());
    }
}
