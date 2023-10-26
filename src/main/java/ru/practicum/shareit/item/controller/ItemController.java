package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
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
    public ItemDtoOut create(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
                             @Validated(CreateValidation.class) @RequestBody ItemDtoIn itemDtoIn) {
        return itemService.create(userId, itemDtoIn);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateById(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
                                 @PathVariable(name = "itemId") @Positive @Min(1) Long itemId,
                                 @RequestBody ItemDtoIn updatedItemDtoIn) {
        return itemService.updateById(ownerId, itemId, updatedItemDtoIn);
    }

    @DeleteMapping("/{itemId}")
    public String deleteById(@PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.deleteById(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getById(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long requestorId,
                              @PathVariable(name = "itemId") @Positive @Min(1) Long itemId) {
        return itemService.getById(requestorId, itemId);
    }

    @GetMapping
    public List<ItemDtoOut> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchForUserByParameter(@RequestParam String text) {
        return itemService.searchForUserByParameter(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long commentatorId,
                                       @PathVariable(name = "itemId") @Positive @Min(1) Long itemId,
                                       @Validated(CreateValidation.class) @RequestBody CommentDtoIn commentDtoIn) {
        return itemService.createComment(commentatorId, itemId, commentDtoIn);
    }
}
