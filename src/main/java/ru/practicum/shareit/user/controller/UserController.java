package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Validated @RequestBody User user) {
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateById(
            @PathVariable(name = "userId") @Positive @Min(1) Long id,
            @Validated @RequestBody User user) {
        return userService.updateById(id, user);
    }

    @DeleteMapping("/{userId}")
    public String deleteById(
            @PathVariable(name = "userId") @Positive @Min(1) Long id
    ) {
        return userService.deleteById(id);
    }

    @GetMapping("/{userId}")
    public UserDto getById(
            @PathVariable(name = "userId") @Positive @Min(1) Long id
    ) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}
