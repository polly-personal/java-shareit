package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDtoOutForBooking;
import ru.practicum.shareit.item.exception.ItemOwnerIdIsNotLinkedToItemId;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    @Override
    public BookingDtoOut create(long bookerId, BookingDtoIn bookingDtoIn) {
        ItemDtoOutForBooking itemDtoOutForBooking = itemService.getByIdForBooking(bookingDtoIn.getItemId());
        long ownerId = itemDtoOutForBooking.getOwner().getId();

        if (ownerId == bookerId) {
            throw new BookingBookerIdIsOwnerId("id бронирующего: " + bookerId + " равно id владельца: " + ownerId);
        }

        LocalDateTime start = bookingDtoIn.getStart();
        LocalDateTime end = bookingDtoIn.getEnd();

        if (start.isAfter(end) || start.isEqual(end)) {
            throw new BookingIncorrectDates("конечная дата НЕ может идти раньше, чем начальная или быть ей равна");
        }

        Booking booking = BookingMapper.toBooking(bookingDtoIn);

        Item item = ItemMapper.toItemDtoOutForBooking(itemDtoOutForBooking);
        booking.setItem(item);
        booking.setBooker(UserMapper.toUser(userService.getById(bookerId)));
        booking.setStatus(Status.WAITING);

        log.info("🟩 пользователем создана бронь (Booking): " + booking);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDtoOut updateStatus(long ownerId, long bookingId, boolean isApproval) {
        Booking updatableBooking = ownerIdIsLinkedBookingId(ownerId, bookingId);
        if (isApproval) {
            if (updatableBooking.getStatus().equals(Status.APPROVED)) {
                throw new BookingStatusAlreadyApproved("статус бронирования уже одобрен: " + updatableBooking);
            }
            updatableBooking.setStatus(Status.APPROVED);
        } else {
            updatableBooking.setStatus(Status.REJECTED);
        }

        Booking approvedBooking = bookingRepository.save(updatableBooking);

        log.info("🟪 обновлено поле \"status\": " + updatableBooking);
        return BookingMapper.toBookingDto(approvedBooking);
    }

    @Override
    public BookingDtoOut getById(long bookingId) {
        Booking issuedBooking =
                bookingRepository.findById(bookingId).orElseThrow(() -> new BookingIdNotFound("введен " +
                        "несуществующий id " +
                        "брони: " + bookingId));

        log.info("🟦 выдана бронь: " + issuedBooking);
        return BookingMapper.toBookingDto(issuedBooking);
    }

    @Override
    public BookingDtoOut getByIdAndOwnerOrBookerId(long ownerOrBookerIdId, long bookingId) {
        Booking issuedBooking = requesterIdIsLinkedBookingId(ownerOrBookerIdId, bookingId);

        log.info("🟦 выдана бронь по id и OwnerOrBookerId: " + issuedBooking);
        return BookingMapper.toBookingDto(issuedBooking);
    }

    @Override
    public List<BookingDtoOut> getAllByBookerId(long bookerId, String state, int from, int size) {
        userService.idIsExists(bookerId);

        /*
        from -- индекс объекта в возвращаемом списке, с кого надо начать отображать список (индексция начинается с 0)
        size -- колво объектов для отбражения на 1ой странице
        ---------
        from / size = номер страницы (Page<Booking> bookings), на которой будут отображены объекты в колве size (индексция начинается с 0)
        например,
            from -- 4
            size -- 2
            №page -- 4/2 = 2
            отображаемые объекты: (№4)Item с id=1
        */
        Page<Booking> bookings;
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("start").descending());

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerId(bookerId, pageRequest);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(bookerId, LocalDateTime.now(), pageRequest);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(bookerId, LocalDateTime.now(), pageRequest);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.WAITING, pageRequest);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.REJECTED, pageRequest);
                break;
            default:
                throw new BookingUnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }

        log.info("🟦 выдан список бронирований: " + bookings);
        return BookingMapper.toBookingsDto(bookings.stream().collect(Collectors.toList()));
    }

    @Override
    public List<BookingDtoOut> getAllByOwnerId(long ownerId, String state, int from, int size) {
        userService.idIsExists(ownerId);

        /*
        from -- индекс объекта в возвращаемом списке, с кого надо начать отображать список (индексция начинается с 0)
        size -- колво объектов для отбражения на 1ой странице
        ---------
        from / size = номер страницы (Page<Booking> bookings), на которой будут отображены объекты в колве size (индексция начинается с 0)
        например,
            from -- 4
            size -- 2
            №page -- 4/2 = 2
            отображаемые объекты: (№4)Item с id=1
        */
        Page<Booking> bookings;
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("start").descending());


        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerId(ownerId, pageRequest);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), pageRequest);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(ownerId, LocalDateTime.now(), pageRequest);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.WAITING, pageRequest);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.REJECTED, pageRequest);
                break;
            default:
                throw new BookingUnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }

        log.info("🟦 выдан список бронирований: " + bookings);
        return BookingMapper.toBookingsDto(bookings.stream().collect(Collectors.toList()));
    }

    private Booking ownerIdIsLinkedBookingId(long ownerId, long bookingId) {
        userService.idIsExists(ownerId);

        Booking issuedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingIdNotFound("введен " +
                "несуществующий id " +
                "брони: " + bookingId));

        Item item = issuedBooking.getItem();
        Long itemId = item.getId();

        if (ownerId != item.getOwner().getId()) {
            throw new ItemOwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }

        return issuedBooking;
    }

    private Booking requesterIdIsLinkedBookingId(long ownerOrBookerIdId, long bookingId) {
        userService.idIsExists(ownerOrBookerIdId);

        Booking issuedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingIdNotFound("введен " +
                "несуществующий id " +
                "брони: " + bookingId));

        long bookerId = issuedBooking.getBooker().getId();
        long ownerId = issuedBooking.getItem().getOwner().getId();

        if (ownerOrBookerIdId != bookerId && ownerOrBookerIdId != ownerId) {
            throw new BookingRequesterIdNotLinkedToBookerIdOrOwnerId("id запрашивающего: " + ownerOrBookerIdId + " не связан с id арендующего или id владельца: " + bookerId);
        }

        return issuedBooking;
    }
}
