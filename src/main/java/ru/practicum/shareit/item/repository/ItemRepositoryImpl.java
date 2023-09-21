package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.NoItemsExistsYet;
import ru.practicum.shareit.item.exception.OwnerIdIsNotLinkedToItemId;
import ru.practicum.shareit.item.exception.ItemIdNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.ThisUserAlreadyExistException;
import ru.practicum.shareit.user.exception.UserErrorIdNotFoundByItemIdException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemMapper itemMapper;

    private Map<Long, Item> items;
    private Long id;

    @Override
    public ItemDto create(Item item) {
        if (items == null) {
            items = new HashMap<>();
        }

        if (!items.containsValue(item)) {
            item.setId(getId());

            Long id = item.getId();
            items.put(id, item);
            log.info("🟩 создана вещь: " + item);

            return itemMapper.toItemDto(item);
        }

        log.info("🟩🟧 вещь НЕ создана: " + item);
        throw new ThisUserAlreadyExistException("у пользователя с id: " + item.getOwner() + " данная вещь уже существует");
    }

    @Override
    public ItemDto updateByItemId(Long id, Item item) {
        String newName = item.getName();
        String newDescription = item.getDescription();
        Boolean newAvailable = item.getAvailable();

        Item updatableItem = items.get(id);
        String updatableName = updatableItem.getName();
        String updatableDescription = updatableItem.getDescription();
        Boolean updatableAvailable = updatableItem.getAvailable();

        if (newName != null && !updatableName.equals(newName)) {
            updatableItem.setName(newName);
            log.info("🟪 обновлено поле \"name\": " + updatableItem);
        }

        if (newDescription != null && !updatableDescription.equals(newDescription)) {
            updatableItem.setDescription(newDescription);
            log.info("🟪 обновлено поле \"description\": " + updatableItem);
        }

        if (newAvailable != null && !updatableAvailable.equals(newAvailable)) {
            updatableItem.setAvailable(newAvailable);
            log.info("🟪 обновлено поле \"available\": " + updatableItem);
        }

        return itemMapper.toItemDto(updatableItem);
    }

    @Override
    public String deleteById(Long id) {
        String responseAndLogging = "⬛️ удалена вещь: " + items.get(id);
        log.info(responseAndLogging);
        items.remove(id);

        return responseAndLogging;
    }

    @Override
    public ItemDto getById(Long id) {
        Item item = items.get(id);
        log.info("🟦 выдана вещь: " + item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllByOwnerId(Long ownerId) {
        if (items != null) {
            List<ItemDto> allItemsForOwner = items.values()
                    .stream()
                    .filter(item -> item.getOwner().equals(ownerId))
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
            log.info("🟦 выдан список вещей: " + allItemsForOwner + ", для владельца с id: " + ownerId);

            return allItemsForOwner;
        }
        throw new NoItemsExistsYet("ни одна вещь еще не добавлена, исправьте это: ➕📦");
    }

    @Override
    public List<ItemDto> searchForUserByParameter(String text) {
        List<ItemDto> itemsByParameter = new ArrayList<>();
        if (items != null && !text.isBlank()) {
            itemsByParameter = items.values()
                    .stream()
                    .filter(item -> {

                        if (item.getAvailable().equals(true)) {
                            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                                return true;
                            }
                            if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                                return true;
                            }
                        }

                        return false;
                    })
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
            log.info("🟦🟦 выдан список вещей: " + itemsByParameter + ", по поиску (параметру): " + text);

            return itemsByParameter;
        }
        return itemsByParameter;
    }

    @Override
    public void ownerIdIsLinkedToItemId(Long ownerId, Long itemId) {
        if (items != null) {
            Optional<Item> itemByOwnerId = items.values()
                    .stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst();

            if (!itemByOwnerId.get().getOwner().equals(ownerId)) {
                throw new OwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
            }
        }
    }

    @Override
    public Long getOwnerByItemIdOrException(Long itemId) {
        if (items != null) {
            Optional<Item> itemByItemId = items.values()
                    .stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst();

            if (!itemByItemId.isEmpty()) {
                return itemByItemId.get().getOwner();
            }
        }
        throw new UserErrorIdNotFoundByItemIdException("по id вещи: " + itemId + ", пользователь не найден");
    }

    @Override
    public void idIsExists(Long id) {
        if (items != null && !items.containsKey(id)) {
            throw new ItemIdNotFoundException("введен несуществующий id вещи: " + id);
        }
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
