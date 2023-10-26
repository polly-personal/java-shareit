package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateValidation;
import ru.practicum.shareit.user.dto.UpdateValidation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.constraints.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public UserDto create(@Validated(CreateValidation.class) @RequestBody UserDto userDto) {
        return userServiceImpl.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateById(@PathVariable(name = "userId") @Positive @Min(1) Long id,
                              @Validated(UpdateValidation.class) @RequestBody UserDto updatedUserDto) {
        return userService.updateById(id, updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public String deleteById(@PathVariable(name = "userId") @Positive @Min(1) Long id) {
        return userService.deleteById(id);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable(name = "userId") @Positive @Min(1) Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}
