package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.RequestHeaders;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.CreateValidation;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Controller
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(RequestHeaders.userId) @Min(1) long userId,
                                         @Validated(CreateValidation.class) @RequestBody ItemRequestDtoIn itemRequestDtoIn) {
        log.info("🟫🟫 POST /requests");
        return itemRequestClient.create(userId, itemRequestDtoIn);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity<Object> getById(@RequestHeader(RequestHeaders.userId) @Min(1) long userId,
                                          @PathVariable @Min(1) long itemRequestId) {
        log.info("🟫🟫 GET /requests/{}", itemRequestId);
        return itemRequestClient.getById(userId, itemRequestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllForRequester(@RequestHeader(RequestHeaders.userId) @Min(1) long userId) {
        log.info("🟫🟫 GET /requests");
        return itemRequestClient.getAllForRequester(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherUsersRequests(@RequestHeader(RequestHeaders.userId) @Min(1) long userId,
                                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                           @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("🟫🟫 GET /requests/all?from={}&size={}", from, size);
        return itemRequestClient.getAllOtherUsersRequests(userId, from, size);
    }
}
