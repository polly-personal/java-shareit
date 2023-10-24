package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.GiveItemDto;
import ru.practicum.shareit.item.dto.CreateValidation;
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
    public GiveItemDto create(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
                              @Validated(CreateValidation.class) @RequestBody GiveItemDto giveItemDto) {
        return itemService.create(userId, giveItemDto);
    }

    @PatchMapping("/{itemId}")
    public GiveItemDto updateById(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
                                  @PathVariable(name = "itemId") @Positive @Min(1) Long itemId,
                                  @RequestBody GiveItemDto updatedGiveItemDto) {
        return itemService.updateById(ownerId, itemId, updatedGiveItemDto);
    }

    @DeleteMapping("/{itemId}")
    public String deleteById(@PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.deleteById(itemId);
    }

    @GetMapping("/{itemId}")
    public GiveItemDto getById(@PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<GiveItemDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<GiveItemDto> searchForUserByParameter(@RequestParam String text) {
        return itemService.searchForUserByParameter(text);
    }
}
