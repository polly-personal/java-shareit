package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDtoOut create(Long userId, ItemDtoIn itemDtoIn);

    ItemDtoOut updateById(Long ownerId, Long id, ItemDtoIn updatedItemDtoIn);

    String deleteById(Long itemId);

    ItemDtoOut getById(Long requestorId, Long itemId);

    ItemDtoOutForBooking getByIdForBooking(Long itemId);

    List<ItemDtoOut> getAllByOwnerId(Long ownerId);

    void idIsExists(Long id);

    List<ItemDtoOut> searchForUserByParameter(String text);

    CommentDtoOut createComment(Long commentatorId, Long itemId, CommentDtoIn commentDtoIn);
}
