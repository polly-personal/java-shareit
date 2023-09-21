package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.OwnerIdIsNotLinkedToItemId;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDto getById(Long bookingId) {
        return bookingRepository.getById(bookingId);
    }

    @Override
    public BookingDto create(Long userId, Booking booking) {
        userRepository.idIsExists(userId);
        Long itemId = booking.getItemId();
        itemRepository.idIsExists(itemId);
        Long ownerId = itemRepository.getOwnerByItemIdOrException(itemId);

        booking.setBooker(userId);
        booking.setOwnerItem(ownerId);

        return bookingRepository.create(booking);
    }

    @Override
    public BookingDto approve(Long ownerId, Long bookingId) {
        ownerIdIsLinkedBookingId(ownerId, bookingId);
        return bookingRepository.approve(bookingId);
    }

    @Override
    public BookingDto reject(Long ownerId, Long bookingId) {
        ownerIdIsLinkedBookingId(ownerId, bookingId);
        return bookingRepository.reject(bookingId);
    }

    @Override
    public BookingDto cancel(Long userId, Long bookingId) {
        userRepository.idIsExists(userId);
        bookingRepository.idIsExists(bookingId);

        return bookingRepository.cancel(bookingId);
    }

    @Override
    public BookingDto createCustomerReview(Long userId, Long bookingId, CustomerReview customerReview) {
        userRepository.idIsExists(userId);
        bookingRepository.idIsExists(bookingId);

        BookingDto bookingDto = bookingRepository.getById(bookingId);
        return bookingRepository.createCustomerReview(bookingDto, customerReview);
    }

    private void ownerIdIsLinkedBookingId(Long ownerId, Long bookingId) {
        userRepository.idIsExists(ownerId);
        bookingRepository.idIsExists(bookingId);

        BookingDto bookingDto = bookingRepository.getById(bookingId);
        Long itemId = bookingDto.getItemId();
        Long ownerIdByItem = itemRepository.getOwnerByItemIdOrException(itemId);

        if (!ownerId.equals(ownerIdByItem)) {
            throw new OwnerIdIsNotLinkedToItemId("id вещи: " + itemId + " не связан с id владельца: " + ownerId);
        }
    }
}
