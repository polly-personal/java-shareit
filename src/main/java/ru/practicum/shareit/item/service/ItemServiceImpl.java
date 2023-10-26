package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserIdNotFound;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ItemRequestService itemRequestService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDtoOut create(Long userId, ItemDtoIn itemDtoIn) {
        userService.idIsExists(userId);
        itemRequestService.idIsExists(itemDtoIn.getItemRequestId());

        Item item = ItemMapper.toItem(itemDtoIn);

        UserDto ownerDto = userService.getById(userId);
        item.setOwner(UserMapper.toUser(ownerDto));
        Item createdItem = itemRepository.save(item);

        log.info("🟩 создана вещь: " + createdItem);
        return ItemMapper.toItemDtoOut(createdItem);
    }

    @Transactional
    @Override
    public ItemDtoOut updateById(Long ownerId, Long itemId, ItemDtoIn updatedItemDtoIn) {
        ownerIdIsLinkedToItemId(ownerId, itemId);

        String newName = updatedItemDtoIn.getName();
        String newDescription = updatedItemDtoIn.getDescription();
        Boolean newAvailable = updatedItemDtoIn.getAvailable();

        Item updatableItem = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));

        String updatableName = updatableItem.getName();
        String updatableDescription = updatableItem.getDescription();
        Boolean updatableAvailable = updatableItem.getAvailable();

        if (newName != null) {
            if (!updatableName.equals(newName)) {
                updatableItem.setName(newName);
                log.info("🟪 обновлено поле \"name\": " + updatableItem);
            }
        }
        if (newDescription != null) {
            if (!updatableDescription.equals(newDescription)) {
                updatableItem.setDescription(newDescription);
                log.info("🟪 обновлено поле \"description\": " + updatableItem);
            }
        }
        if (newAvailable != null) {
            if (!updatableAvailable.equals(newAvailable)) {
                updatableItem.setAvailable(newAvailable);
                log.info("🟪 обновлено поле \"available\": " + updatableItem);
            }
        }

        Item updatedItem = itemRepository.save(updatableItem);
        return ItemMapper.toItemDtoOut(updatedItem);
    }

    @Transactional
    @Override
    public String deleteById(Long itemId) {
        itemRepository.deleteById(itemId);

        String responseAndLogging;
        responseAndLogging = "⬛️ удалена вещь по itemId: " + itemId;

        log.info(responseAndLogging);
        return responseAndLogging;
    }

    @Override
    public ItemDtoOut getById(Long requestorId, Long itemId) {
        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));

        ItemDtoOut itemDtoOut = ItemMapper.toItemDtoOut(issuedItem);

        if (requestorId.equals(issuedItem.getOwner().getId())) {
            itemDtoOut.setLastBooking(bookingRepository.findFirstByItemIdAndStartBeforeOrderByEndDesc(itemId, LocalDateTime.now()));
            itemDtoOut.setNextBooking(bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId,
                    LocalDateTime.now(), Status.APPROVED));
        }

        itemDtoOut.setComments(CommentMapper.toCommentsDto(commentRepository.findAllByItemId(itemId)));

        log.info("🟦 выдана вещь: " + itemDtoOut);
        return itemDtoOut;
    }

    @Override
    public ItemDtoOutForBooking getByIdForBooking(Long itemId) {
        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));

        log.info("🟦 выдана вещь (ItemDtoOutForBooking) для BookingService: " + issuedItem);
        return ItemMapper.toItemDtoOutForBooking(issuedItem);
    }

    @Override
    public List<ItemDtoOut> getAllByOwnerId(Long ownerId) {
        List<Item> issuedItems = itemRepository.findAllByOwnerId(ownerId).orElseThrow(() -> new ItemNoItemsExistsYet("ни одна вещь еще не добавлена, исправьте это: ➕📦"));

        List<ItemDtoOut> itemsDtoOut = ItemMapper.toItemsDtoOut(issuedItems);

        List<ItemDtoOut> result = itemsDtoOut.stream()
                .peek(itemDtoOut -> {
                            Long itemId = itemDtoOut.getId();
                            itemDtoOut.setLastBooking(bookingRepository.findFirstByItemIdAndStartBeforeOrderByEndDesc(itemId, LocalDateTime.now()));
                            itemDtoOut.setNextBooking(bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, LocalDateTime.now(), Status.APPROVED));
                            itemDtoOut.setComments(CommentMapper.toCommentsDto(commentRepository.findAllByItemId(itemId)));
                        }
                )
                .collect(Collectors.toList());

        log.info("🟦 выдан список вещей: " + result + ", для владельца с id: " + ownerId);
        return result;
    }

    @Override
    public void idIsExists(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new UserIdNotFound("введен несуществующий id вещи: " + id);
        }
    }

    @Override
    public List<ItemDtoOut> searchForUserByParameter(String text) {
        List<Item> issuedItems = new ArrayList<>();

        if (text != null && !text.isBlank()) {

            issuedItems = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
            log.info("🟦🟦 выдан список вещей: " + issuedItems + ", по поиску (параметру): " + text);
        } else {

            log.info("🟦🟦 выдан ПУСТОЙ список вещей: " + issuedItems + ", по поиску (параметру): \"" + text + "\"");
        }

        return ItemMapper.toItemsDtoOut(issuedItems);
    }

    @Transactional
    @Override
    public CommentDtoOut createComment(Long commentatorId, Long itemId, CommentDtoIn commentDtoIn) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));

        UserDto formerBooker = userService.getById(commentatorId);

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatusEquals(commentatorId, itemId, LocalDateTime.now(), Status.APPROVED);

        if (!bookings.isEmpty()) {

            Comment comment = CommentMapper.toComment(commentDtoIn);

            comment.setItem(item);
            comment.setAuthor(UserMapper.toUser(formerBooker));
            comment.setCreated(LocalDateTime.now());

            log.info("🟩 создан комментарий: " + comment);
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        }
        throw new ItemCommentatorIdNotHaveCompletedBooking("комментатор не имеет завершенной брони");
    }

    private void ownerIdIsLinkedToItemId(Long ownerId, Long itemId) {
        userService.idIsExists(ownerId);

        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));
        if (!ownerId.equals(issuedItem.getOwner().getId())) {
            throw new ItemOwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }
    }
}
