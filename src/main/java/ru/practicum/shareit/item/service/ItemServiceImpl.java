package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.exception.ThisUserAlreadyExistException;
import ru.practicum.shareit.user.exception.UserIdNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        userService.idIsExists(userId);
        itemRequestService.idIsExists(itemDto.getRequestId());

        Item itemFromDto = ItemMapper.toItem(itemDto);
        itemFromDto.setOwner(userId);

        if (!itemRepository.itemIsExists(itemFromDto)) {
            Item createdItem = itemRepository.create(itemFromDto);
            log.info("🟩 создана вещь: " + createdItem);

            return ItemMapper.toItemDto(createdItem);
        }

        log.info("🟩🟧 вещь НЕ создана: " + itemDto);

        throw new ThisUserAlreadyExistException("у пользователя с id: " + itemFromDto.getOwner() + " данная вещь уже существует");
    }

    @Override
    public ItemDto updateById(Long ownerId, Long itemId, ItemDto updatedItemDto) {
        ownerIdIsLinkedToItemId(ownerId, itemId);

        String newName = updatedItemDto.getName();
        String newDescription = updatedItemDto.getDescription();
        Boolean newAvailable = updatedItemDto.getAvailable();

        Item updatableItem = itemRepository.getById(itemId);
        updatableItem.setOwner(ownerId);

        String updatableName = updatableItem.getName();
        String updatableDescription = updatableItem.getDescription();
        Boolean updatableAvailable = updatableItem.getAvailable();

        if (newName != null) {
            if (!updatableName.equals(newName)) {
                updatableItem.setName(newName);
                log.info("🟪 обновлено поле \"name\": " + updatableItem);
            }
        } else {
            log.info("🟪🟧 поле \"name\" НЕ обновлено: " + updatableItem);
        }
        if (newDescription != null) {
            if (!updatableDescription.equals(newDescription)) {
                updatableItem.setDescription(newDescription);
                log.info("🟪 обновлено поле \"description\": " + updatableItem);
            }
        } else {
            log.info("🟪🟧 поле \"description\" НЕ обновлено: " + updatableItem);
        }
        if (newAvailable != null) {
            if (!updatableAvailable.equals(newAvailable)) {
                updatableItem.setAvailable(newAvailable);
                log.info("🟪 обновлено поле \"available\": " + updatableItem);
            }
        } else {
            log.info("🟪🟧 поле \"available\" НЕ обновлено: " + updatableItem);
        }

        Item updatedItem = itemRepository.updateById(itemId, updatableItem);

        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public String deleteById(Long itemId) {
        Item deletedItem = itemRepository.deleteById(itemId);

        String responseAndLogging;
        if (deletedItem != null) {
            responseAndLogging = "⬛️ удалена вещь: " + deletedItem;
            log.info(responseAndLogging);

        } else {
            responseAndLogging = "⬛️ вещь не удалена: " + deletedItem;
            log.info(responseAndLogging);

            throw new UserIdNotFoundException("введен несуществующий id вещи: " + itemId);
        }

        return responseAndLogging;
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item issuedItem = itemRepository.getById(itemId);
        if (issuedItem != null) {
            log.info("🟦 выдана вещь: " + issuedItem);

            return ItemMapper.toItemDto(issuedItem);

        } else {
            log.info("🟦 вещь НЕ выдана: " + issuedItem);

            throw new UserIdNotFoundException("введен несуществующий id вещи: " + itemId);
        }
    }

    @Override
    public List<ItemDto> getAllByOwnerId(Long ownerId) {
        userService.idIsExists(ownerId);

        List<Item> issuedItems = itemRepository.getAllByOwnerId(ownerId);

        if (issuedItems != null) {
            log.info("🟦 выдан список вещей: " + issuedItems + ", для владельца с id: " + ownerId);

            return ItemMapper.toItemsDto(issuedItems);

        } else {
            log.info("🟦 вещь НЕ выдана: " + issuedItems);

            throw new NoItemsExistsYet("ни одна вещь еще не добавлена, исправьте это: ➕📦");
        }
    }

    @Override
    public void idIsExists(Long id) {
        if (!itemRepository.idIsExists(id)) {
            throw new UserIdNotFoundException("введен несуществующий id вещи: " + id);
        }
    }

    @Override
    public List<ItemDto> searchForUserByParameter(String text) {
        List<Item> issuedItems = new ArrayList<>();

        if (text != null && !text.isBlank()) {

            issuedItems = itemRepository.searchForUserByParameter(text);
            log.info("🟦🟦 выдан список вещей: " + issuedItems + ", по поиску (параметру): " + text);

        } else {
            log.info("🟦🟦 выдан ПУСТОЙ список вещей: " + issuedItems + ", по поиску (параметру): \"" + text + "\"");
        }

        return ItemMapper.toItemsDto(issuedItems);
    }

    private void ownerIdIsLinkedToItemId(Long ownerId, Long itemId) {
        userService.idIsExists(ownerId);
        itemRepository.idIsExists(itemId);

        Long ownerIdByItem = userService.getOwnerByItemIdOrThrow(itemId);

        if (!ownerId.equals(ownerIdByItem)) {
            throw new OwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }
    }
}
