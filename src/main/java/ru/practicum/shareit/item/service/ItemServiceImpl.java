package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemAvailableIsEmptyException;
import ru.practicum.shareit.item.exception.ItemDescriptionIsEmptyException;
import ru.practicum.shareit.item.exception.ItemNameIsEmptyException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto create(Long userId, Item item) {

        userRepository.idIsExists(userId);

        Long itemRequestId = item.getRequestId();
        if (itemRequestId != null) {
            itemRequestRepository.idIsExists(itemRequestId);
            item.setRequestId(itemRequestId);
        }

        nameNotNull(item.getName());
        descriptionNotNull(item.getDescription());
        availableNotNull(item.getAvailable());

        item.setOwner(userId);

        return itemRepository.create(item);
    }

    @Override
    public ItemDto updateById(Long ownerId, Long itemId, Item item) {
        userRepository.idIsExists(ownerId);
        itemRepository.idIsExists(itemId);
        itemRepository.ownerIdIsLinkedToItemId(ownerId, itemId);

        item.setOwner(ownerId);
        item.setId(itemId);

        return itemRepository.updateByItemId(itemId, item);
    }

    @Override
    public String deleteById(Long ownerId, Long itemId) {
        userRepository.idIsExists(ownerId);
        itemRepository.idIsExists(itemId);

        return itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDto getById(Long ownerId, Long itemId) {
        userRepository.idIsExists(ownerId);
        itemRepository.idIsExists(itemId);

        return itemRepository.getById(itemId);
    }

    @Override
    public List<ItemDto> getAllByOwnerId(Long ownerId) {
        userRepository.idIsExists(ownerId);
        return itemRepository.getAllByOwnerId(ownerId);
    }

    @Override
    public List<ItemDto> searchForUserByUserIdAndParameter(Long userId, String text) {
        userRepository.idIsExists(userId);
        return itemRepository.searchForUserByParameter(text);
    }


    private void nameNotNull(String name) {
        if (name == null || name.isBlank()) {
            throw new ItemNameIsEmptyException("поле \"name\" должно быть заполнено");
        }
    }

    private void descriptionNotNull(String description) {
        if (description == null || description.isBlank()) {
            throw new ItemDescriptionIsEmptyException("поле \"description\" должно быть заполнено");
        }
    }

    private void availableNotNull(Boolean available) {
        if (available == null) {
            throw new ItemAvailableIsEmptyException("поле \"available\" должно быть заполнено");
        }
    }
}
