package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateValidation;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@RestController
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private static final String requestHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDtoOut create(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                                    @Validated(CreateValidation.class) @RequestBody ItemRequestDtoIn itemRequestDtoIn) {
        log.info("🟫 POST /requests");
        return itemRequestService.create(userId, itemRequestDtoIn);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDtoOut getById(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                                     @PathVariable @Positive @Min(1) long itemRequestId) {
        log.info("🟫 GET /requests/{}", itemRequestId);
        userService.idIsExists(userId);
        return itemRequestService.getById(itemRequestId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getAllForRequester(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId) {
        log.info("🟫 GET /requests");
        return itemRequestService.getAllForRequester(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllOtherUsersRequests(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                            @RequestParam(defaultValue = "10") @Positive @Min(1) int size) {
        log.info("🟫 GET /requests/all");
        return itemRequestService.getAllOtherUsersRequests(userId, from, size);
    }
}
