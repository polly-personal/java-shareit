package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateValidation;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

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

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long userId,
                                 @Validated(CreateValidation.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("🟫 POST /requests");
        return itemRequestService.create(userId, itemRequestDto);
    }
}
