package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@RestController
public class ItemController {
    private final ItemService itemService;
    private static final String requestHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemDtoOut create(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                             @Validated(CreateValidation.class) @RequestBody ItemDtoIn itemDtoIn) {
        log.info("🟫 POST /items");
        return itemService.create(userId, itemDtoIn);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateById(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long ownerId,
                                 @PathVariable(name = "itemId") @Positive @Min(1) long itemId,
                                 @RequestBody ItemDtoIn updatedItemDtoIn) {
        log.info("🟫 PATCH /items/{}", itemId);
        return itemService.updateById(ownerId, itemId, updatedItemDtoIn);
    }

    @DeleteMapping("/{itemId}")
    public String deleteById(@PathVariable(name = "itemId") @Positive @Min(1) long itemId) {
        log.info("🟫 DELETE /items/{}", itemId);
        return itemService.deleteById(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getById(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                              @PathVariable(name = "itemId") @Positive @Min(1) long itemId) {
        log.info("🟫 GET /items/{}", itemId);
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoOut> getAllByOwnerId(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long ownerId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive @Min(1) int size) {
        log.info("🟫 GET /items?from={}&size={}", from, size);
        return itemService.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchForUserByParameter(@RequestParam String text,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "10") @Positive @Min(1) int size) {
        log.info("🟫 GET /items/search?text={}&from={}&size={}", text, from, size);
        return itemService.searchForUserByParameter(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long bookerId,
                                       @PathVariable(name = "itemId") @Positive @Min(1) long itemId,
                                       @Validated(CreateValidation.class) @RequestBody CommentDtoIn commentDtoIn) {
        log.info("🟫 POST /items/{}/comment", itemId);
        return itemService.createComment(bookerId, itemId, commentDtoIn);
    }
}
