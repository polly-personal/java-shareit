package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    ItemDto create(Item item);

    ItemDto updateByItemId(Long id, Item item);

    String deleteById(Long id);

    ItemDto getById(Long id);

    List<ItemDto> getAllByOwnerId(Long ownerId);

    List<ItemDto> searchForUserByParameter(String text);

    void ownerIdIsLinkedToItemId(Long ownerId, Long itemId);

    Long getOwnerByItemIdOrException(Long itemId);

    void idIsExists(Long id);
}
