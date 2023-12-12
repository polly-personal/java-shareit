package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.CreateValidation;
import ru.practicum.shareit.user.dto.UpdateValidation;
import ru.practicum.shareit.user.dto.UserDtoIn;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Controller
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(CreateValidation.class) @RequestBody UserDtoIn userDtoIn) {
        log.info("🟫🟫 POST /users");
        return userClient.create(userDtoIn);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateById(@PathVariable(name = "userId") @Positive @Min(1) long id,
                                             @Validated(UpdateValidation.class) @RequestBody UserDtoIn updatedUserDtoIn) {
        log.info("🟫🟫 PATCH /users/{}", id);
        return userClient.updateById(id, updatedUserDtoIn);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "userId") @Positive @Min(1) long id) {
        log.info("🟫🟫 DELETE /users/{}", id);
        return userClient.deleteById(id);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable(name = "userId") @Positive @Min(1) long id) {
        log.info("🟫🟫 GET /users/{}", id);
        return userClient.getById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("🟫🟫 GET /users");
        return userClient.getAll();
    }
}
