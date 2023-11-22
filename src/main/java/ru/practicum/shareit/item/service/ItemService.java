package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDtoOut create(long userId, ItemDtoIn itemDtoIn);

    ItemDtoOut updateById(long ownerId, long id, ItemDtoIn updatedItemDtoIn);

    String deleteById(long itemId);

    ItemDtoOut getById(long requesterId, long itemId);

    ItemDtoOutForBooking getByIdForBooking(long itemId);

    List<ItemDtoOut> getAllByOwnerId(long ownerId, int from, int size);

    void idIsExists(Long id);

    List<ItemDtoOut> searchForUserByParameter(String text, int from, int size);

    CommentDtoOut createComment(long bookerId, long itemId, CommentDtoIn commentDtoIn);
}
