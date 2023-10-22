package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto updateById(Long ownerId, Long id, ItemDto updatedItemDto);

    String deleteById(Long itemId);

    ItemDto getById(Long itemId);

    List<ItemDto> getAllByOwnerId(Long ownerId);

    void idIsExists(Long id);

    List<ItemDto> searchForUserByParameter(String text);
}
