package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Item updateById(Long id, Item item);

    Item deleteById(Long id);

    Item getById(Long id);

    List<Item> getAllByOwnerId(Long ownerId);

    boolean idIsExists(Long id);

    boolean itemIsExists(Item item);

    List<Item> searchForUserByParameter(String text);

    Optional<Item> getOptionalOwnerByItemId(Long itemId);
}
