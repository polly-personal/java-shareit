package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@RestController
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
            @RequestBody Item item) {
        return itemService.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(
            @RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
            @PathVariable(name = "itemId") @Positive @Min(1) Long itemId,
            @RequestBody Item item) {
        return itemService.updateById(ownerId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public String deleteById(
            @RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
            @PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.deleteById(ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(
            @RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
            @PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.getById(ownerId, itemId);
    }


    @GetMapping
    public List<ItemDto> getAllByOwnerId(
            @RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId
    ) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchForUserByUserIdAndParameter(
            @RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
            @RequestParam String text
    ) {
        return itemService.searchForUserByUserIdAndParameter(userId, text);
    }
}
