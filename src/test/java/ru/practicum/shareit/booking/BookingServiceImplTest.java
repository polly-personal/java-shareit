package ru.practicum.shareit.booking;

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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.exception.BookingBookerIdIsOwnerId;
import ru.practicum.shareit.booking.exception.BookingIdNotFound;
import ru.practicum.shareit.booking.exception.BookingIncorrectDates;
import ru.practicum.shareit.booking.exception.BookingRequesterIdNotLinkedToBookerIdOrOwnerId;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserIdNotFound;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("BookingServiceImplTest должен ")
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    User itemOwner;
    User booker;
    Item item;
    LocalDateTime current = LocalDateTime.of(2023, 11, 10, 01, 01, 01);
    Booking currentBooking;

    int from = 0;
    int size = 10;
    PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("start").descending());

    @BeforeEach
    void initEntities() {
        itemOwner = User.builder().id(1L).name("test_name_1").email("test_email_1").build();
        booker = User.builder().id(2L).name("test_name_2").email("test_email_2").build();
        item =
                Item.builder().id(1L).name("test_name_1").description("test_description_1").available(true).owner(itemOwner).build();
        currentBooking =
                Booking.builder().id(2L).start(current).end(current.plusDays(9)).item(item).booker(booker).status(Status.WAITING).build();
    }

    @DisplayName("сохранять бронь по id бронирующего")
    @Test
    void create_whenSuccessInvoked_thenCreatedBookingIsReturned() {
        when(itemService.getByIdForBooking(anyLong())).thenReturn(ItemMapper.toItemDtoOutForBooking(item));
        when(userService.getById(anyLong())).thenReturn(UserMapper.toUserDto(booker));
        when(bookingRepository.save(any())).thenReturn(currentBooking);

        BookingDtoIn bookingDtoIn = BookingMapper.toBookingDtoIn(currentBooking);
        BookingDtoOut returnedBookingDtoOut = bookingServiceImpl.create(anyLong(), bookingDtoIn);

        BookingDtoOut bookingDtoOut = BookingMapper.toBookingDto(currentBooking);
        Assertions.assertEquals(bookingDtoOut, returnedBookingDtoOut);
    }

    @DisplayName("НЕ сохранять бронь по id бронирующего, если этот id является id'шником владельца_вещи")
    @Test
    void create_whenBookingBookerIdIsOwnerId_thenCreatedBookingIsNotReturned() {
        when(itemService.getByIdForBooking(item.getId())).thenReturn(ItemMapper.toItemDtoOutForBooking(item));

        BookingDtoIn bookingDtoIn = BookingMapper.toBookingDtoIn(currentBooking);
        Assertions.assertThrows(BookingBookerIdIsOwnerId.class, () -> bookingServiceImpl.create(itemOwner.getId(), bookingDtoIn));
    }

    @DisplayName("НЕ сохранять бронь по id бронирующего, если конечная дата идет раньше, чем начальная")
    @Test
    void create_whenBookingIncorrectDates_thenCreatedBookingIsNotReturned() {
        when(itemService.getByIdForBooking(item.getId())).thenReturn(ItemMapper.toItemDtoOutForBooking(item));

        BookingDtoIn bookingDtoIn = BookingMapper.toBookingDtoIn(currentBooking);
        bookingDtoIn.setEnd(currentBooking.getEnd().minusDays(30));
        Assertions.assertThrows(BookingIncorrectDates.class, () -> bookingServiceImpl.create(anyLong(), bookingDtoIn));
    }

    @DisplayName("обновлять статус_брони по id владельца_вещи и самой брони")
    @Test
    void updateStatus_whenSuccessInvoked_thenUpdatedBookingIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        when(bookingRepository.findById(currentBooking.getId())).thenReturn(Optional.of(currentBooking));
        when(bookingRepository.save(any())).thenReturn(currentBooking);

        BookingDtoOut returnedBookingDtoOut = bookingServiceImpl.updateStatus(itemOwner.getId(), currentBooking.getId(), true);

        BookingDtoOut bookingDtoOut = BookingMapper.toBookingDto(currentBooking);
        Assertions.assertEquals(bookingDtoOut, returnedBookingDtoOut);
    }

    @DisplayName("выдавать бронь по полю \"id\"")
    @Test
    void getById_whenSuccessInvoked_thenIssuedBookingIsReturned() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(currentBooking));

        BookingDtoOut returnedBookingDtoOut = bookingServiceImpl.getById(anyLong());

        BookingDtoOut bookingDtoOut = BookingMapper.toBookingDto(currentBooking);
        Assertions.assertEquals(bookingDtoOut, returnedBookingDtoOut);
    }

    @DisplayName("НЕ выдавать предмет по полю \"id\" для владельца, если этот id НЕ найден в бд")
    @Test
    void getById_whenSuccessInvoked_thenIssuedBookingIsNotReturned() {
        when(bookingRepository.findById(anyLong())).thenThrow(BookingIdNotFound.class);

        Assertions.assertThrows(BookingIdNotFound.class, () -> bookingServiceImpl.getById(anyLong()));
    }

    @DisplayName("выдавать бронь по id владельца_вещи или бронирующего_вещь и по id самой брони")
    @Test
    void getByIdAndOwnerOrBookerId_whenSuccessInvoked_thenBookingIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        when(bookingRepository.findById(currentBooking.getId())).thenReturn(Optional.of(currentBooking));

        BookingDtoOut returnedBookingDtoOut = bookingServiceImpl.getByIdAndOwnerOrBookerId(itemOwner.getId(), currentBooking.getId());

        BookingDtoOut bookingDtoOut = BookingMapper.toBookingDto(currentBooking);
        Assertions.assertEquals(bookingDtoOut, returnedBookingDtoOut);
    }

    @DisplayName("НЕ выдавать бронь по id владельца_вещи или бронирующего_вещь и по id самой брони, если id запрашивающего НЕ связан с id арендующего или id владельца")
    @Test
    void getByIdAndOwnerOrBookerId_whenBookingRequesterIdNotLinkedToBookerIdOrOwnerId_thenBookingIsNotReturned() {
        doNothing().when(userService).idIsExists(99L);
        when(bookingRepository.findById(currentBooking.getId())).thenReturn(Optional.of(currentBooking));

        Assertions.assertThrows(BookingRequesterIdNotLinkedToBookerIdOrOwnerId.class, () -> bookingServiceImpl.getByIdAndOwnerOrBookerId(99L, currentBooking.getId()));
    }

    @DisplayName("выдавать ALL брони по id бронирующего_вещь")
    @Test
    void getAllByBookerId_whenSuccessInvoked_thenAllBookingsIsReturned() {
        doNothing().when(userService).idIsExists(booker.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByBookerId(booker.getId(), pageRequest)).thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByBookerId(booker.getId(), "ALL", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать CURRENT брони по id бронирующего_вещь")
    @Test
    void getAllByBookerId_whenSuccessInvoked_thenCurrentBookingsIsReturned() {
        doNothing().when(userService).idIsExists(booker.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByBookerId(booker.getId(), "CURRENT", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать PAST брони по id бронирующего_вещь")
    @Test
    void getAllByBookerId_whenSuccessInvoked_thenPastBookingsIsReturned() {
        doNothing().when(userService).idIsExists(booker.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByBookerIdAndEndBefore(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByBookerId(booker.getId(), "PAST", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать FUTURE брони по id бронирующего_вещь")
    @Test
    void getAllByBookerId_whenSuccessInvoked_thenFutureBookingsIsReturned() {
        doNothing().when(userService).idIsExists(booker.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByBookerIdAndStartAfter(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByBookerId(booker.getId(), "FUTURE", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать WAITING брони по id бронирующего_вещь")
    @Test
    void getAllByBookerId_whenSuccessInvoked_thenWaitingBookingsIsReturned() {
        doNothing().when(userService).idIsExists(booker.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByBookerId(booker.getId(), "WAITING", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать REJECTED брони по id бронирующего_вещь")
    @Test
    void getAllByBookerId_whenSuccessInvoked_thenRejectedBookingsIsReturned() {
        doNothing().when(userService).idIsExists(booker.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByBookerId(booker.getId(), "REJECTED", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("НЕ выдавать брони по id бронирующего_вещь, если этот id НЕ найден в бд")
    @Test
    void getAllByBookerId_whenUserIdNotFound_thenBookingIsNotReturned() {
        doThrow(UserIdNotFound.class).when(userService).idIsExists(anyLong());

        Assertions.assertThrows(UserIdNotFound.class, () -> bookingServiceImpl.getAllByBookerId(anyLong(), "ALL", from, size));
    }

    @DisplayName("выдавать ALL брони по id владельца_вещи")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenAllBookingsIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByItemOwnerId(itemOwner.getId(), pageRequest)).thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByOwnerId(itemOwner.getId(), "ALL", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать CURRENT брони по id владельца_вещи")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenCurrentBookingsIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByOwnerId(itemOwner.getId(), "CURRENT", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать PAST брони по id владельца_вещи")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenPastBookingsIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByItemOwnerIdAndEndBefore(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByOwnerId(itemOwner.getId(), "PAST", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать FUTURE брони по id владельца_вещи")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenFutureBookingsIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByItemOwnerIdAndStartAfter(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByOwnerId(itemOwner.getId(), "FUTURE", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать WAITING брони по id владельца_вещи")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenWaitingBookingsIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByOwnerId(itemOwner.getId(), "WAITING", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }

    @DisplayName("выдавать REJECTED брони по id владельца_вещи")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenRejectedBookingsIsReturned() {
        doNothing().when(userService).idIsExists(itemOwner.getId());
        Page<Booking> bookings = new PageImpl<>(List.of(currentBooking));
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDtoOut> returnedBookingsDtoOut = bookingServiceImpl.getAllByOwnerId(itemOwner.getId(), "REJECTED", from, size);

        List<BookingDtoOut> bookingsDtoOut = BookingMapper.toBookingsDto(List.of(currentBooking));
        Assertions.assertEquals(bookingsDtoOut, returnedBookingsDtoOut);
    }
}
