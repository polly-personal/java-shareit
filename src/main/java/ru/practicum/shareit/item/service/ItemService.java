package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.GiveItemDto;

import java.util.List;

public interface ItemService {
    GiveItemDto create(Long userId, GiveItemDto giveItemDto);

    GiveItemDto updateById(Long ownerId, Long id, GiveItemDto updatedGiveItemDto);

    String deleteById(Long itemId);

    GiveItemDto getById(Long itemId);

    List<GiveItemDto> getAllByOwnerId(Long ownerId);

    void idIsExists(Long id);

    List<GiveItemDto> searchForUserByParameter(String text);
}
