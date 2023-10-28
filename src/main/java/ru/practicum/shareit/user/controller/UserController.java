package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public UserDto create(@Validated(CreateValidation.class) @RequestBody UserDto userDto) {
        log.info("🟫 POST /users");
        return userServiceImpl.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateById(@PathVariable(name = "userId") @Positive @Min(1) long id,
                              @Validated(UpdateValidation.class) @RequestBody UserDto updatedUserDto) {
        log.info("🟫 PATCH /users/{}", id);
        return userService.updateById(id, updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public String deleteById(@PathVariable(name = "userId") @Positive @Min(1) long id) {
        log.info("🟫 DELETE /users/{}", id);
        return userService.deleteById(id);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable(name = "userId") @Positive @Min(1) long id) {
        log.info("🟫 GET /users/{}", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("🟫 GET /users");
        return userService.getAll();
    }
}
