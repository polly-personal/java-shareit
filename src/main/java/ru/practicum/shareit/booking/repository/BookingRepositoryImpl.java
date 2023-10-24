package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.constant.Status;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private Map<Long, Booking> bookings = new HashMap<>();
    private Long id;

    @Override
    public Booking create(Booking booking) {
        booking.setId(getId());
        booking.setStatus(Status.WAITING);
        bookings.put(booking.getId(), booking);

        return booking;
    }

    @Override
    public Booking approve(Long bookingId) {
        Booking booking = bookings.get(bookingId);
        booking.setStatus(Status.APPROVED);
        log.info("🟩🟪 владельцем подтверждена бронь (Booking): " + booking);

        return booking;
    }

    @Override
    public Booking reject(Long bookingId) {
        Booking booking = bookings.get(bookingId);
        booking.setStatus(Status.REJECTED);
        log.info("🟩🟧 владельцем отклонена бронь (Booking): " + booking);

        return booking;
    }

    @Override
    public Booking cancel(Long bookingId) {
        Booking booking = bookings.get(bookingId);

        booking.setStatus(Status.CANCELED);
        log.info("🟩🟥 пользователем отменена (из map НЕ удалена) бронь (Booking): " + booking);

        /*bookings.remove(bookingId);*/
        return booking;
    }

    @Override
    public Booking getById(Long bookingId) {
        Booking booking = bookings.get(bookingId);
        log.info("🟦 выдана бронь (Booking): " + booking);

        return booking;
    }

    @Override
    public boolean idIsExists(Long id) {
        return bookings.containsKey(id);
    }

    @Override
    public boolean bookingIsExists(Booking booking) {
        return bookings.containsValue(booking);
    }

    @Override
    public Booking createCustomerReview(Booking booking, CustomerReview customerReview) {
        Booking bookingFromDataBase = bookings.get(booking.getId());
        /*bookingFromDataBase.setCustomerReview(customerReview);*/

        log.info("🟢 пользователем создан отзыв: " + customerReview);

        return bookingFromDataBase;
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
