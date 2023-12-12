package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CreateValidation;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@Controller
public class ItemController {
    private final ItemClient itemClient;
    private static final String requestHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                                         @Validated(CreateValidation.class) @RequestBody ItemDtoIn itemDtoIn) {
        log.info("🟫🟫 POST /items");
        return itemClient.create(userId, itemDtoIn);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateById(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long ownerId,
                                             @PathVariable(name = "itemId") @Positive @Min(1) long itemId,
                                             @RequestBody ItemDtoIn updatedItemDtoIn) {
        log.info("🟫🟫 PATCH /items/{}", itemId);
        return itemClient.updateById(ownerId, itemId, updatedItemDtoIn);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "itemId") @Positive @Min(1) long itemId) {
        log.info("🟫🟫 DELETE /items/{}", itemId);
        return itemClient.deleteById(itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                                          @PathVariable(name = "itemId") @Positive @Min(1) long itemId) {
        log.info("🟫🟫 GET /items/{}", itemId);
        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByOwnerId(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long ownerId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive @Min(1) int size) {
        log.info("🟫🟫 GET /items?from={}&size={}", from, size);
        return itemClient.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchForUserByParameter(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long userId,
                                                           @RequestParam String text,
                                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                           @RequestParam(defaultValue = "10") @Positive @Min(1) int size) {
        log.info("🟫🟫 GET /items/search?text={}&from={}&size={}", text, from, size);
        return itemClient.searchForUserByParameter(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(requestHeaderUserId) @Positive @Min(1) long bookerId,
                                                @PathVariable(name = "itemId") @Positive @Min(1) long itemId,
                                                @Validated(CreateValidation.class) @RequestBody CommentDtoIn commentDtoIn) {
        log.info("🟫🟫 POST /items/{}/comment", itemId);
        return itemClient.createComment(bookerId, itemId, commentDtoIn);
    }
}
