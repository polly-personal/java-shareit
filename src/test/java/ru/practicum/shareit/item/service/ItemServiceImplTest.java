package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDtoOutForItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.exception.ItemIdNotFound;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exception.ItemRequestIdNotFound;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserIdNotFound;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("ItemServiceImplTest должен ")
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ItemServiceImpl itemServiceImpl;

    User owner;
    Item item;
    int from = 0;
    int size = 10;
    PageRequest pageRequest = PageRequest.of(from, size);

    @BeforeEach
    void initEntities() {
        owner = User.builder().id(1L).name("test_name_1").email("test_email_1").build();
        item = Item.builder().id(1L).name("test_name_1").description("test_description_1").available(true).owner(owner).build();
    }

    @DisplayName("сохранять предмет по id пользователя")
    @Test
    void create_whenSuccessInvoked_thenCreatedItemIsReturned() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.toUserDto(owner));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDtoIn itemDtoIn = ItemDtoIn.builder().name("test_name_1").description("test_description_1").available(true).build();
        ItemDtoOut returnedItemDtoOut = itemServiceImpl.create(anyLong(), itemDtoIn);

        ItemDtoOut itemDtoOut =
                ItemDtoOut.builder().id(1L).name("test_name_1").description("test_description_1").available(true).build();
        Assertions.assertEquals(itemDtoOut, returnedItemDtoOut);
    }

    @DisplayName("НЕ сохранять предмет по id пользователя, если этот id НЕ найден в бд")
    @Test
    void create_whenUserIdNotFound_thenCreatedItemIsNotReturned() {
        when(userService.getById(anyLong())).thenThrow(UserIdNotFound.class);

        ItemDtoIn itemDtoIn = ItemDtoIn.builder().name("test_name_1").description("test_description_1").available(true).build();
        Assertions.assertThrows(UserIdNotFound.class, () -> itemServiceImpl.create(anyLong(), itemDtoIn));
    }

    @DisplayName("сохранять предмет по id пользователя в ответ на запрос (ItemRequest)")
    @Test
    void create_whenSuccessInvoked_thenCreatedItemOnRequestIsReturned() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.toUserDto(owner));

        item.setItemRequest(ItemRequest.builder().id(1L).build());
        when(itemRepository.save(any())).thenReturn(item);

        ItemDtoIn itemDtoIn = ItemDtoIn.builder().name("test_name_1").description("test_description_1").available(true).build();
        ItemDtoOut returnedItemDtoOut = itemServiceImpl.create(anyLong(), itemDtoIn);

        ItemDtoOut itemDtoOut =
                ItemDtoOut.builder().id(1L).name("test_name_1").description("test_description_1").available(true).requestId(1L).build();
        Assertions.assertEquals(itemDtoOut, returnedItemDtoOut);
    }

    @DisplayName("НЕ сохранять предмет по id пользователя в ответ на запрос (ItemRequest), если id_запроса НЕ найден в бд")
    @Test
    void create_whenItemRequestIdNotFound_thenCreatedItemOnRequestIsNotReturned() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.toUserDto(owner));
        when(itemRequestRepository.findById(anyLong())).thenThrow(ItemRequestIdNotFound.class);

        ItemDtoIn itemDtoIn =
                ItemDtoIn.builder().name("test_name_1").description("test_description_1").available(true).requestId(1L).build();
        Assertions.assertThrows(ItemRequestIdNotFound.class, () -> itemServiceImpl.create(anyLong(), itemDtoIn));
    }

    @DisplayName("обновлять предмет по id пользователя и предмета")
    @Test
    void updateById_whenSuccessInvoked_thenUpdatedItemIsReturned() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        item.setDescription("test_update-description_1");
        when(itemRepository.save(any())).thenReturn(item);

        ItemDtoIn updatedItemDtoIn =
                ItemDtoIn.builder().name("test_name_1").description("test_update-description_1").available(true).build();
        ItemDtoOut returnedItemDtoOut = itemServiceImpl.updateById(owner.getId(), anyLong(), updatedItemDtoIn);

        ItemDtoOut itemDtoOut = ItemDtoOut.builder().id(1L).name("test_name_1").description("test_update-description_1").available(true).build();
        Assertions.assertEquals(itemDtoOut, returnedItemDtoOut);
    }

    @DisplayName("НЕ обновлять предмет по id пользователя и предмета, если id_предмета НЕ найден в бд")
    @Test
    void updateById_whenItemIdNotFound_thenUpdatedItemIsNotReturned() {
        when(itemRepository.findById(anyLong())).thenThrow(ItemIdNotFound.class);

        ItemDtoIn updatedItemDtoIn = ItemDtoIn.builder().name("test_name_1").description("test_update-description_1").available(true).build();
        Assertions.assertThrows(ItemIdNotFound.class, () -> itemServiceImpl.updateById(owner.getId(), anyLong(), updatedItemDtoIn));
    }

    @DisplayName("удалять предмет по полю \"id\"")
    @Test
    void deleteById_whenSuccessInvoked_thenStringResponseIsReturned() {
        doNothing().when(itemRepository).deleteById(anyLong());

        String returnedResponse = itemServiceImpl.deleteById(anyLong());
        String response = "⬛️ удалена вещь по itemId: 0";
        Assertions.assertEquals(response, returnedResponse);
    }

    @DisplayName("выдавать предмет по полю \"id\" для владельца")
    @Test
    void getById_whenSuccessInvoked_thenIssuedItemWithBookingsAndCommentsIsReturned() {
        when(userService.getById(owner.getId())).thenReturn(UserMapper.toUserDto(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 01, 01, 00, 00, 00))
                .end(LocalDateTime.of(2023, 01, 9, 00, 00, 00))
                .item(item)
                .booker(owner)
                .build();
        when(bookingRepository.findFirstByItemIdAndStartBeforeOrderByEndDesc(
                anyLong(),
                any()))
                .thenReturn(Optional.of(lastBooking));
        Booking nextBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 01, 20, 00, 00, 00))
                .end(LocalDateTime.of(2023, 01, 29, 00, 00, 00))
                .item(item)
                .booker(owner)
                .status(Status.APPROVED)
                .build();
        when(bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                anyLong(),
                any(),
                any()))
                .thenReturn(Optional.of(nextBooking));

        User booker = User.builder().id(2L).name("test_name_2").email("test_email_2").build();
        Comment comment = Comment.builder()
                .id(1L)
                .text("test_comment_1")
                .item(item)
                .author(booker)
                .created(LocalDateTime.of(2023, 01, 20, 00, 00, 00))
                .build();
        when(commentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of(comment));


        ItemDtoOut returnedItemDtoOut = itemServiceImpl.getById(owner.getId(), anyLong());

        BookingDtoOutForItem lastBookingDtoOutForItem = BookingMapper.toBookingDtoForItem(lastBooking);
        BookingDtoOutForItem nextBookingDtoOutForItem = BookingMapper.toBookingDtoForItem(nextBooking);
        CommentDtoOut commentDtoOut = CommentMapper.toCommentDtoOut(comment);
        List<CommentDtoOut> commentsDtoOut = new ArrayList<>();
        commentsDtoOut.add(commentDtoOut);
        ItemDtoOut issuedItemDtoOut = ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBookingDtoOutForItem)
                .nextBooking(nextBookingDtoOutForItem)
                .comments(commentsDtoOut)
                .build();
        Assertions.assertEquals(issuedItemDtoOut, returnedItemDtoOut);
    }

    @DisplayName("выдавать все предметы по полю \"id\" для владельца")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenIssuedItemsWithBookingsIsReturned() {
        Page<Item> items = new PageImpl<>(List.of(item));
        when(itemRepository.findAllByOwnerId(owner.getId(), pageRequest)).thenReturn(items);

        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 01, 01, 00, 00, 00))
                .end(LocalDateTime.of(2023, 01, 9, 00, 00, 00))
                .item(item)
                .booker(owner)
                .build();
        when(bookingRepository.findFirstByItemIdInAndStartBeforeOrderByEndDesc(
                any(),
                any()))
                .thenReturn(Optional.of(lastBooking));
        Booking nextBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 01, 20, 00, 00, 00))
                .end(LocalDateTime.of(2023, 01, 29, 00, 00, 00))
                .item(item)
                .booker(owner)
                .status(Status.APPROVED)
                .build();
        when(bookingRepository.findFirstByItemIdInAndStartAfterAndStatusOrderByStartAsc(
                any(),
                any(),
                any()))
                .thenReturn(Optional.of(nextBooking));

        List<ItemDtoOut> returnedItemsDtoOut = itemServiceImpl.getAllByOwnerId(owner.getId(), from, size);

        Assertions.assertNotNull(returnedItemsDtoOut);
    }

    @DisplayName("проверять предмет на наличие в бд по полю \"id\" (результат: найден)")
    @Test
    void idIsExists_whenSuccessInvoked_thenExceptionIsNotReturned() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);

        itemServiceImpl.idIsExists(anyLong());
    }

    @DisplayName("проверять предмет на наличие в бд по полю \"id\" (результат: НЕ найден)")
    @Test
    void idIsExists_whenIdNotFound_thenExceptionIsReturned() {
        when(itemRepository.existsById(anyLong())).thenThrow(ItemIdNotFound.class);

        Assertions.assertThrows(ItemIdNotFound.class, () -> itemServiceImpl.idIsExists(anyLong()));
    }

    @DisplayName("выдавать все предметы по поиску содеражщегося слова в полях \"name\" и \"description\"")
    @Test
    void searchForUserByParameter_whenSuccessInvoked_thenIssuedItemsIsReturned() {
        String searchText = "teSt";
        Page<Item> issuedItems = new PageImpl<>(List.of(item));
        when(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(searchText,
                searchText, pageRequest)).thenReturn(issuedItems);

        List<ItemDtoOut> returnedItemsDtoOut = itemServiceImpl.searchForUserByParameter(searchText, from, size);

        Assertions.assertNotNull(returnedItemsDtoOut);
    }

    @DisplayName("сохранять комментарий по id пользователя и предмета")
    @Test
    void createComment_whenSuccessInvoked_thenCreatedCommentIsReturned() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.toUserDto(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 01, 01, 00, 00, 00))
                .end(LocalDateTime.of(2023, 01, 9, 00, 00, 00))
                .item(item)
                .booker(owner)
                .status(Status.APPROVED)
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(lastBooking);
        when(bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatusEquals(anyLong(), anyLong(), any(), eq(Status.APPROVED))).thenReturn(bookings);

        User booker = User.builder().id(2L).name("test_name_2").email("test_email_2").build();
        Comment comment = Comment.builder()
                .id(1L)
                .text("test_comment_1")
                .item(item)
                .author(booker)
                .created(LocalDateTime.of(2023, 01, 20, 00, 00, 00))
                .build();
        when(commentRepository.save(any())).thenReturn(comment);

        CommentDtoIn commentDtoIn = CommentMapper.toCommentDtoIn(comment);
        CommentDtoOut returnedCommentDtoOut = itemServiceImpl.createComment(owner.getId(), item.getId(), commentDtoIn);
        Assertions.assertEquals(CommentMapper.toCommentDtoOut(comment), returnedCommentDtoOut);
    }

}
