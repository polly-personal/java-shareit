package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDtoOut create(long userId, ItemDtoIn itemDtoIn);

    ItemDtoOut updateById(long ownerId, long id, ItemDtoIn updatedItemDtoIn);

    String deleteById(long itemId);

    ItemDtoOut getById(long requestorId, long itemId);

    ItemDtoOutForBooking getByIdForBooking(long itemId);

    List<ItemDtoOut> getAllByOwnerId(long ownerId);

    void idIsExists(Long id);

    List<ItemDtoOut> searchForUserByParameter(String text);

    CommentDtoOut createComment(long commentatorId, long itemId, CommentDtoIn commentDtoIn);
}
