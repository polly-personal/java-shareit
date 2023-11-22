package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDtoOutForItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
import ru.practicum.shareit.request.exception.ItemRequestIdNotFound;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserIdNotFound;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public ItemDtoOut create(long userId, ItemDtoIn itemDtoIn) {
        Item item = ItemMapper.toItem(itemDtoIn);

        UserDto ownerDto = userService.getById(userId);
        item.setOwner(UserMapper.toUser(ownerDto));

        Long itemRequestId = itemDtoIn.getRequestId();
        if (itemRequestId != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(() ->
                    new ItemRequestIdNotFound("введен несуществующий id запроса вещи (itemRequestId): " + itemRequestId));
            item.setItemRequest(itemRequest);
        }

        Item createdItem = itemRepository.save(item);

        log.info("🟩 создана вещь: " + createdItem);
        return ItemMapper.toItemDtoOut(createdItem);
    }

    @Transactional
    @Override
    public ItemDtoOut updateById(long ownerId, long itemId, ItemDtoIn updatedItemDtoIn) {
        ownerIdIsLinkedToItemId(ownerId, itemId);

        String newName = updatedItemDtoIn.getName();
        String newDescription = updatedItemDtoIn.getDescription();
        Boolean newAvailable = updatedItemDtoIn.getAvailable();

        Item updatableItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemIdNotFound("введен несуществующий id " +
                "вещи: " + itemId));

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
    public String deleteById(long itemId) {
        itemRepository.deleteById(itemId);

        String responseAndLogging;
        responseAndLogging = "⬛️ удалена вещь по itemId: " + itemId;

        log.info(responseAndLogging);
        return responseAndLogging;
    }

    @Override
    public ItemDtoOut getById(long userId, long itemId) {
        userService.getById(userId);
        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));

        ItemDtoOut itemDtoOut = ItemMapper.toItemDtoOut(issuedItem);

        if (userId == issuedItem.getOwner().getId()) {
            Booking lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeOrderByEndDesc(itemId, LocalDateTime.now()).orElse(null);

            BookingDtoOutForItem lastBookingDtoOutForItem = null;
            if (lastBooking != null) {
                lastBookingDtoOutForItem = BookingMapper.toBookingDtoForItem(lastBooking);
            }

            Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, LocalDateTime.now(), Status.APPROVED).orElse(null);

            BookingDtoOutForItem nextBookingDtoOutForItem = null;
            if (nextBooking != null) {
                nextBookingDtoOutForItem = BookingMapper.toBookingDtoForItem(nextBooking);
            }


            itemDtoOut.setLastBooking(lastBookingDtoOutForItem);
            itemDtoOut.setNextBooking(nextBookingDtoOutForItem);
        }

        itemDtoOut.setComments(CommentMapper.toCommentsDtoOut(commentRepository.findAllByItemId(itemId)));

        log.info("🟦 выдана вещь: " + itemDtoOut);
        return itemDtoOut;
    }

    @Override
    public ItemDtoOutForBooking getByIdForBooking(long itemId) {
        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemIdNotFound("введен несуществующий" +
                " id вещи: " + itemId)); /* требуется статус 404 */
        if (issuedItem.getAvailable() == false) { /* требуется статус 400 */
            throw new ItemNotAvailableForBooking("вещь недоступна для бронирования");
        }

        log.info("🟦 выдана вещь (ItemDtoOutForBooking) для BookingService: " + issuedItem);
        return ItemMapper.toItemDtoOutForBooking(issuedItem);
    }

    @Override
    public List<ItemDtoOut> getAllByOwnerId(long ownerId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);

        Page<Item> items = itemRepository.findAllByOwnerId(ownerId, pageRequest);
        if (items.isEmpty()) {
            throw new ItemNoItemsExistsYet("ни одна вещь еще не добавлена, исправьте это: ➕📦");
        }
        List<Long> itemIds = new ArrayList<>();
        items.stream().map(item -> itemIds.add(item.getId())).collect(Collectors.toList());

        List<BookingDtoOutForItem> lastBookings =
                bookingRepository.findFirstByItemIdInAndStartBeforeOrderByEndDesc(itemIds, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDtoForItem)
                        .collect(Collectors.toList());
        Map<Long, BookingDtoOutForItem> lastBookingToItemId = new HashMap<>();
        for (BookingDtoOutForItem lastBooking : lastBookings) {
            lastBookingToItemId.put(lastBooking.getItemId(), lastBooking);
        }

        List<BookingDtoOutForItem> nextBookings =
                bookingRepository.findFirstByItemIdInAndStartAfterAndStatusOrderByStartAsc(itemIds, LocalDateTime.now(),
                                Status.APPROVED)
                        .stream()
                        .map(BookingMapper::toBookingDtoForItem)
                        .collect(Collectors.toList());
        Map<Long, BookingDtoOutForItem> nextBookingToItemId = new HashMap<>();
        for (BookingDtoOutForItem nextBooking : nextBookings) {
            nextBookingToItemId.put(nextBooking.getItemId(), nextBooking);
        }

        List<ItemDtoOut> itemsDtoOut = ItemMapper.toItemsDtoOut(items.toList())
                .stream().map(itemDtoOut -> {
                    itemDtoOut.setLastBooking(lastBookingToItemId.get(itemDtoOut.getId()));
                    itemDtoOut.setNextBooking(nextBookingToItemId.get(itemDtoOut.getId()));

                    return itemDtoOut;
                }).collect(Collectors.toList());

        log.info("🟦 выдан список вещей: " + itemsDtoOut + ", для владельца с id: " + ownerId);
        return itemsDtoOut;
    }

    @Override
    public void idIsExists(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ItemIdNotFound("введен несуществующий id вещи: " + id);
        }
    }

    @Override
    public List<ItemDtoOut> searchForUserByParameter(String text, int from, int size) {
        if (text != null && !text.isBlank()) {
            PageRequest pageRequest = PageRequest.of(from, size);

            Page<Item> issuedItems = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text, pageRequest);
            log.info("🟦🟦 выдан список вещей: " + issuedItems + ", по поиску (параметру): \"" + text + "\"");

            return ItemMapper.toItemsDtoOut(issuedItems.toList());
        } else {
            log.info("🟦🟦 выдан ПУСТОЙ список вещей: [], по поиску (параметру): \"" + text + "\"");

            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public CommentDtoOut createComment(long bookerId, long itemId, CommentDtoIn commentDtoIn) {
        UserDto formerBooker = userService.getById(bookerId);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatusEquals(bookerId, itemId, LocalDateTime.now(), Status.APPROVED);

        if (!bookings.isEmpty()) {

            Comment comment = CommentMapper.toComment(commentDtoIn);

            comment.setItem(item);
            comment.setAuthor(UserMapper.toUser(formerBooker));
            comment.setCreated(LocalDateTime.now());

            log.info("🟩 создан комментарий: " + comment);
            return CommentMapper.toCommentDtoOut(commentRepository.save(comment));
        }
        throw new ItemCommentatorIdNotHaveCompletedBooking("комментатор не имеет завершенной брони");
    }

    private void ownerIdIsLinkedToItemId(long ownerId, long itemId) {
        userService.idIsExists(ownerId);

        Item issuedItem = itemRepository.findById(itemId).orElseThrow(() -> new UserIdNotFound("введен несуществующий id вещи: " + itemId));
        if (ownerId != issuedItem.getOwner().getId()) {
            throw new ItemOwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }
    }
}
