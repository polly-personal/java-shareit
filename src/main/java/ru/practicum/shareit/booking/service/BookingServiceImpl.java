package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.dto.GiveBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.OwnerIdIsNotLinkedToItemId;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.ThisUserAlreadyExistException;
import ru.practicum.shareit.user.exception.UserIdNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public GiveBookingDto create(Long userId, GiveBookingDto giveBookingDto) {
       /* userService.idIsExists(userId);*/
        Long itemId = giveBookingDto.getItemId();
        /*itemService.idIsExists(itemId);*/
     /*   Long ownerId = userService.getOwnerByItemIdOrThrow(itemId);*/

        Booking bookingFromDto = BookingMapper.toBooking(giveBookingDto);
        /*bookingFromDto.setBooker(userId);
        bookingFromDto.setOwnerItem(ownerId);*/

        if (!bookingRepository.bookingIsExists(bookingFromDto)) {
            Booking createdBooking = bookingRepository.create(bookingFromDto);
            log.info("🟩 пользователем создана бронь (Booking): " + bookingFromDto);

            /*return BookingMapper.toBookingDto(createdBooking);*/
            return null;
        }

        log.info("🟩🟧 бронь (Booking) пользователя НЕ создана: " + giveBookingDto);
        /*throw new ThisUserAlreadyExistException("пользователь с id: " + giveBookingDto.getBooker() + " уже создал бронь этой" +
                " вещи");*/
        return null;

    }

    @Override
    public GiveBookingDto approve(Long ownerId, Long bookingId) {
        ownerIdIsLinkedBookingId(ownerId, bookingId);
        Booking approvedBooking = bookingRepository.approve(bookingId);

        /*return BookingMapper.toBookingDto(approvedBooking);*/
        return null;
    }

    @Override
    public GiveBookingDto reject(Long ownerId, Long bookingId) {
        ownerIdIsLinkedBookingId(ownerId, bookingId);
        Booking rejectedBooking = bookingRepository.reject(bookingId);

       /* return BookingMapper.toBookingDto(rejectedBooking);*/
        return null;
    }

    @Override
    public GiveBookingDto cancel(Long userId, Long bookingId) {
      /*  userRepository.idIsExists(userId);*/
        bookingRepository.idIsExists(bookingId);

        Booking canceledBooking = bookingRepository.cancel(bookingId);

        /*return BookingMapper.toBookingDto(canceledBooking);*/
        return null;
    }

    @Override
    public GiveBookingDto getById(Long bookingId) {
        Booking issuedBooking = bookingRepository.getById(bookingId);

        /*return BookingMapper.toBookingDto(issuedBooking);*/
        return null;
    }

    @Override
    public void idIsExists(Long id) {
        if (!bookingRepository.idIsExists(id)) {
            throw new UserIdNotFoundException("введен несуществующий id  брони (Booking): " + id);
        }
    }

    @Override
    public GiveBookingDto createCustomerReview(Long bookingId, CustomerReview customerReview) {
        Booking bookingFromDataBase = BookingMapper.toBooking(getById(bookingId));
        Booking bookingWithCustomerReview = bookingRepository.createCustomerReview(bookingFromDataBase, customerReview);

        /*return BookingMapper.toBookingDto(bookingWithCustomerReview);*/
        return null;
    }

    private void ownerIdIsLinkedBookingId(Long ownerId, Long bookingId) {
       /* userService.idIsExists(ownerId);*/
        Booking issuedBooking = BookingMapper.toBooking(getById(bookingId));

        /*Long itemId = issuedBooking.getItemId();
        Long ownerIdByItem = userService.getOwnerByItemIdOrThrow(itemId);

        if (!ownerId.equals(ownerIdByItem)) {
            throw new OwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }*/
    }
}
