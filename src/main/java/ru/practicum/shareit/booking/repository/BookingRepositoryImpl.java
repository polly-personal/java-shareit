package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingIdNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.user.exception.ThisUserAlreadyExistException;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final BookingMapper bookingMapper;
    private Map<Long, Booking> bookings;
    private Long id;

    @Override
    public BookingDto create(Booking booking) {
        if (bookings == null) {
            bookings = new HashMap<>();
        }

        if (!bookings.containsValue(booking)) {
            booking.setId(getId());
            booking.setStatus(Status.WAITING);

            Long id = booking.getId();
            bookings.put(id, booking);
            log.info("🟩 пользователем создана бронь (Booking): " + booking);

            return bookingMapper.toBookingDto(booking);
        }

        log.info("🟩🟧 бронь (Booking) пользователя НЕ создана: " + booking);
        throw new ThisUserAlreadyExistException("пользователь с id: " + booking.getBooker() + " уже создал бронь этой" +
                " вещи");
    }

    @Override
    public BookingDto approve(Long bookingId) {
        Booking booking = bookings.get(bookingId);
        booking.setStatus(Status.APPROVED);
        log.info("🟩🟪 владельцем подтверждена бронь (Booking): " + booking);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto reject(Long bookingId) {
        Booking booking = bookings.get(bookingId);
        booking.setStatus(Status.REJECTED);
        log.info("🟩🟧 владельцем отклонена бронь (Booking): " + booking);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto cancel(Long bookingId) {
        Booking booking = bookings.get(bookingId);

        booking.setStatus(Status.CANCELED);
        log.info("🟩🟥 пользователем отменена (из map НЕ удалена) бронь (Booking): " + booking);

        /*bookings.remove(bookingId);*/
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        return bookingDto;
    }

    @Override
    public BookingDto getById(Long bookingId) {
        Booking booking = bookings.get(bookingId);
        log.info("🟦 выдана бронь (Booking): " + booking);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto createCustomerReview(BookingDto bookingDto, CustomerReview customerReview) {
        Booking booking = bookings.get(bookingDto.getId());
        booking.setCustomerReview(customerReview);

        log.info("🟢 пользователем создан отзыв: " + customerReview);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public void idIsExists(Long id) {
        if (bookings != null && !bookings.containsKey(id)) {
            throw new BookingIdNotFoundException("введен несуществующий id брони (Booking): " + id);
        }
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
