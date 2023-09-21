package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-item-requests.
 */
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@RestController
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(
            @RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
            @Validated @RequestBody ItemRequest itemRequest
    ) {
        return itemRequestService.create(userId, itemRequest);
    }
}
