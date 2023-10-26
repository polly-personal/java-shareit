package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingUnsupportedState;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDtoOutForBooking;
import ru.practicum.shareit.item.exception.ItemOwnerIdIsNotLinkedToItemId;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private static final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @Transactional
    @Override
    public BookingDtoOut create(Long bookerId, BookingDtoIn bookingDtoIn) {
        ItemDtoOutForBooking itemDtoOutForBooking = itemService.getByIdForBooking(bookingDtoIn.getItemId());
        Long ownerId = itemDtoOutForBooking.getOwner().getId();

        if (ownerId.equals(bookerId)) {
            throw new BookingBookerIdIsOwnerId("id бронирующего: " + bookerId + " равно id владельца: " + ownerId);
        }

        if (itemDtoOutForBooking.getAvailable().equals(true)) {

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

        log.info("🟩🟧 бронь (Booking) пользователя НЕ создана: " + bookingDtoIn);
        throw new BookingNotAvailable("вещь НЕ доступна для бронирования");
    }

    @Transactional
    @Override
    public BookingDtoOut updateStatus(Long ownerId, Long bookingId, boolean isApproval) {
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
    public BookingDtoOut getById(Long bookingId) {
        Booking issuedBooking =
                bookingRepository.findById(bookingId).orElseThrow(() -> new BookingIdNotFound("введен " +
                        "несуществующий id " +
                        "брони: " + bookingId));

        log.info("🟦 выдана бронь: " + issuedBooking);
        return BookingMapper.toBookingDto(issuedBooking);
    }

    @Override
    public BookingDtoOut getByIdAndRequestorId(Long requestorId, Long bookingId) {
        Booking issuedBooking = requestorIdIsLinkedBookingId(requestorId, bookingId);

        log.info("🟦 выдана бронь по id и RequestorId: " + issuedBooking);
        return BookingMapper.toBookingDto(issuedBooking);
    }

    @Override
    public List<BookingDtoOut> getAllByBookerId(Long bookerId, String state) {
        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerId(bookerId, sort);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, LocalDateTime.now(), LocalDateTime.now(), sort);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(bookerId, LocalDateTime.now(), sort);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(bookerId, LocalDateTime.now(), sort);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.WAITING,
                        sort);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.REJECTED,
                        sort);
                break;
            default:
                throw new BookingUnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }

        log.info("🟦 выдан список бронирований: " + bookings);
        return BookingMapper.toBookingsDto(bookings);
    }

    @Override
    public List<BookingDtoOut> getAllByOwnerId(Long ownerId, String state) {
        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerId(ownerId, sort);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now(), LocalDateTime.now(), sort);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), sort);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(ownerId, LocalDateTime.now(), sort);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.WAITING,
                        sort);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.REJECTED,
                        sort);
                break;
            default:
                throw new BookingUnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }

        log.info("🟦 выдан список бронирований: " + bookings);
        return BookingMapper.toBookingsDto(bookings);
    }

    private Booking ownerIdIsLinkedBookingId(Long ownerId, Long bookingId) {
        userService.idIsExists(ownerId);

        Booking issuedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingIdNotFound("введен " +
                "несуществующий id " +
                "брони: " + bookingId));

        Item item = issuedBooking.getItem();
        Long itemId = item.getId();

        if (!ownerId.equals(item.getOwner().getId())) {
            throw new ItemOwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }

        return issuedBooking;
    }

    private Booking requestorIdIsLinkedBookingId(Long requestorId, Long bookingId) {
        userService.idIsExists(requestorId);

        Booking issuedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingIdNotFound("введен " +
                "несуществующий id " +
                "брони: " + bookingId));

        Long bookerId = issuedBooking.getBooker().getId();
        Long ownerId = issuedBooking.getItem().getOwner().getId();

        if (!requestorId.equals(bookerId) && !requestorId.equals(ownerId)) {
            throw new BookingRequestorIdNotLinkedToBookerIdOrOwnerId("id запрашивающего: " + requestorId + " не связан с id арендующего или id владельца: " + bookerId);
        }

        return issuedBooking;
    }
}
