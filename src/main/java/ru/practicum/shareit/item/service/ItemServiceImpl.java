package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.GiveItemDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserIdNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final ItemRepository itemRepository;

    @Override
    public GiveItemDto create(Long userId, GiveItemDto giveItemDto) {
        userService.idIsExists(userId);
        itemRequestService.idIsExists(giveItemDto.getItemRequestId());

        Item item = ItemMapper.toItem(giveItemDto);

        UserDto ownerDto = userService.getById(userId);
//        item.setOwnerId(userId);
        item.setOwner(UserMapper.toUser(ownerDto));
//        if (!itemRepository.itemIsExists(item)) {
//            Item createdItem = itemRepository.create(item);
        Item createdItem = itemRepository.save(item);
        log.info("🟩 создана вещь: " + createdItem);

        return ItemMapper.toItemDto(createdItem);
//        }
//        log.info("🟩🟧 вещь НЕ создана: " + giveItemDto);
//        throw new ThisUserAlreadyExistException("у пользователя с id: " + item.getOwnerId() + " данная вещь уже существует");
//        return null;
    }

    @Override
    public GiveItemDto updateById(Long ownerId, Long itemId, GiveItemDto updatedGiveItemDto) {
        ownerIdIsLinkedToItemId(ownerId, itemId);

        String newName = updatedGiveItemDto.getName();
        String newDescription = updatedGiveItemDto.getDescription();
        Boolean newAvailable = updatedGiveItemDto.getAvailable();

//        Item updatableItem = itemRepository.getById(itemId);
        Item updatableItem = itemRepository.findById(itemId).orElseThrow(() ->
                new UserIdNotFoundException("введен несуществующий id вещи: " + itemId));
//        updatableItem.setOwnerId(ownerId);

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

//        Item updatedItem = itemRepository.updateById(itemId, updatableItem);
        Item updatedItem = itemRepository.save(updatableItem);
        return ItemMapper.toItemDto(updatedItem);
//        return null;
    }

    @Override
    public String deleteById(Long itemId) {
//        Item deletedItem = itemRepository.deleteById(itemId);
        itemRepository.deleteById(itemId);

        String responseAndLogging;
//        if (deletedItem != null) {
//            responseAndLogging = "⬛️ удалена вещь: " + deletedItem;
        responseAndLogging = "⬛️ удалена вещь по itemId: " + itemId;
        log.info(responseAndLogging);
//
//        } else {
//            responseAndLogging = "⬛️ вещь не удалена: " + deletedItem;
//            log.info(responseAndLogging);
//
//            throw new UserIdNotFoundException("введен несуществующий id вещи: " + itemId);
//        }
//
        return responseAndLogging;
    }

    @Override
    public GiveItemDto getById(Long itemId) {
//        Item issuedItem = itemRepository.getById(itemId);
        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() ->
                new UserIdNotFoundException("введен несуществующий id вещи: " + itemId));
//        if (issuedItem != null) {
        log.info("🟦 выдана вещь: " + issuedItem);

        return ItemMapper.toItemDto(issuedItem);

//        } else {
//            log.info("🟦 вещь НЕ выдана: " + issuedItem);
//
//            throw new UserIdNotFoundException("введен несуществующий id вещи: " + itemId);
//        }
    }

    @Override
    public List<GiveItemDto> getAllByOwnerId(Long ownerId) {
        userService.idIsExists(ownerId);

//        List<Item> issuedItems = itemRepository.getAllByOwnerId(ownerId);
        List<Item> issuedItems = itemRepository.findAllByOwnerId(ownerId).orElseThrow(() -> new NoItemsExistsYet("ни одна вещь еще не добавлена, исправьте это: ➕📦"));

//        if (issuedItems != null) {
        log.info("🟦 выдан список вещей: " + issuedItems + ", для владельца с id: " + ownerId);

        return ItemMapper.toItemsDto(issuedItems);
//
//        } else {
//            log.info("🟦 вещь НЕ выдана: " + issuedItems);
//
//            throw new NoItemsExistsYet("ни одна вещь еще не добавлена, исправьте это: ➕📦");
//        }
    }

    @Override
    public void idIsExists(Long id) {
//        if (!itemRepository.idIsExists(id)) {
        if (!itemRepository.existsById(id)) {
            throw new UserIdNotFoundException("введен несуществующий id вещи: " + id);
        }
    }

    @Override
    public List<GiveItemDto> searchForUserByParameter(String text) {
//        List<Item> issuedItems = new ArrayList<>();
        Set<Item> issuedItems = new HashSet<>();

        if (text != null && !text.isBlank()) {

            String searchText = text.toLowerCase();
//            issuedItems = itemRepository.findAllBySearchText(searchText);
//            issuedItems = itemRepository.findAllByNameContainingIgnoreCase(searchText);
            issuedItems =
                    itemRepository.findAllByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(text, text);
            System.out.println("🟧" + issuedItems);

            log.info("🟦🟦 выдан список вещей: " + issuedItems + ", по поиску (параметру): " + text);

        } else {
            log.info("🟦🟦 выдан ПУСТОЙ список вещей: " + issuedItems + ", по поиску (параметру): \"" + text + "\"");
        }

//        return ItemMapper.toItemsDto(issuedItems);
        return null;
    }

    private void ownerIdIsLinkedToItemId(Long ownerId, Long itemId) {
        userService.idIsExists(ownerId);
//        itemRepository.idIsExists(itemId);

//        Long ownerIdByItem = userService.getOwnerIdByItemIdOrThrow(itemId);

        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() ->
                new UserIdNotFoundException("введен несуществующий id вещи: " + itemId));
//        System.out.println("🔴🔴🔴" + issuedItem.getOwner().getId());

//        if (!ownerId.equals(ownerIdByItem)) {
        if (!ownerId.equals(issuedItem.getOwner().getId())) {
            throw new OwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }

    }
}
