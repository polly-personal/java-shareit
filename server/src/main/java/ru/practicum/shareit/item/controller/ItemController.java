package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@RestController
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private static final String requestHeaderUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemDtoOut create(@RequestHeader(requestHeaderUserId) long userId,
                             @RequestBody ItemDtoIn itemDtoIn) {
        log.info("🟫 POST /items");
        return itemService.create(userId, itemDtoIn);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateById(@RequestHeader(requestHeaderUserId) long ownerId,
                                 @PathVariable(name = "itemId") long itemId,
                                 @RequestBody ItemDtoIn updatedItemDtoIn) {
        log.info("🟫 PATCH /items/{}", itemId);
        return itemService.updateById(ownerId, itemId, updatedItemDtoIn);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable(name = "itemId") long itemId) {
        log.info("🟫 DELETE /items/{}", itemId);
        itemService.deleteById(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getById(@RequestHeader(requestHeaderUserId) long userId,
                              @PathVariable(name = "itemId") long itemId) {
        log.info("🟫 GET /items/{}", itemId);
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoOut> getAllByOwnerId(@RequestHeader(requestHeaderUserId) long ownerId,
                                            @RequestParam int from,
                                            @RequestParam int size) {
        log.info("🟫 GET /items?from={}&size={}", from, size);
        return itemService.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchForUserByParameter(@RequestHeader(requestHeaderUserId) long userId,
                                                     @RequestParam String text,
                                                     @RequestParam int from,
                                                     @RequestParam int size) {
        log.info("🟫 GET /items/search?text={}&from={}&size={}", text, from, size);
        userService.idIsExists(userId);
        return itemService.searchForUserByParameter(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(requestHeaderUserId) long bookerId,
                                       @PathVariable(name = "itemId") long itemId,
                                       @RequestBody CommentDtoIn commentDtoIn) {
        log.info("🟫 POST /items/{}/comment", itemId);
        return itemService.createComment(bookerId, itemId, commentDtoIn);
    }
}
