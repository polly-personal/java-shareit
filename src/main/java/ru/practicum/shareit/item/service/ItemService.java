package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, Item item);

    ItemDto updateById(Long ownerId, Long id, Item item);

    String deleteById(Long ownerId, Long itemId);

    ItemDto getById(Long ownerId, Long itemId);

    List<ItemDto> getAllByOwnerId(Long ownerId);

    List<ItemDto> searchForUserByUserIdAndParameter(Long userId, String text);
}
