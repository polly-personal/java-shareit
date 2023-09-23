package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MainValidation;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@RestController
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
                          @Validated(MainValidation.class) @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
                              @PathVariable(name = "itemId") @Positive @Min(1) Long itemId,
                              @RequestBody ItemDto updatedItemDto) {
        return itemService.updateById(ownerId, itemId, updatedItemDto);
    }

    @DeleteMapping("/{itemId}")
    public String deleteById(@PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.deleteById(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchForUserByUserIdAndParameter(@RequestParam String text) {
        return itemService.searchForUserByUserIdAndParameter(text);
    }
}
