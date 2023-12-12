package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("🟫 POST /users");
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateById(@PathVariable(name = "userId") long id,
                              @RequestBody UserDto updatedUserDto) {
        log.info("🟫 PATCH /users/{}", id);
        return userService.updateById(id, updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable(name = "userId") long id) {
        log.info("🟫 DELETE /users/{}", id);
        userService.deleteById(id);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable(name = "userId") long id) {
        log.info("🟫 GET /users/{}", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("🟫 GET /users");
        return userService.getAll();
    }
}
