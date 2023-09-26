package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long, Item> items = new HashMap<>();
    private Long id;

    @Override
    public Item create(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Item updateById(Long id, Item updatedItem) {
        items.put(id, updatedItem);
        return updatedItem;
    }

    @Override
    public Item deleteById(Long id) {
        Item deletedItem = items.remove(id);

        return deletedItem;
    }

    @Override
    public Item getById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllByOwnerId(Long ownerId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean idIsExists(Long id) {
        return items.containsKey(id);
    }

    @Override
    public boolean itemIsExists(Item item) {
        return items.containsValue(item);
    }

    @Override
    public List<Item> searchForUserByParameter(String text) {
        return items.values()
                .stream()
                .filter(item -> {

                    if (item.getAvailable().equals(true)) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            return true;
                        }
//                        if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
//                            return true;
//                        }
                        return item.getDescription().toLowerCase().contains(text.toLowerCase());
                    }

                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getOptionalOwnerByItemId(Long itemId) {
        return items.values()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
