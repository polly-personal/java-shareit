package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ALL
    Page<Booking> findAllByBookerId(long bookerId, Pageable pageRequest);

    // CURRENT
    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageRequest);

    // PAST
    Page<Booking> findAllByBookerIdAndEndBefore(long bookerId, LocalDateTime dateTime, Pageable pageRequest);

    // FUTURE
    Page<Booking> findAllByBookerIdAndStartAfter(long bookerId, LocalDateTime dateTime, Pageable pageRequest);

    // WAITING / REJECTED
    Page<Booking> findAllByBookerIdAndStatusEquals(long bookerId, Status status, Pageable pageRequest);

    // ALL
    Page<Booking> findAllByItemOwnerId(long bookerId, Pageable pageRequest);

    // CURRENT
    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageRequest);

    // PAST
    Page<Booking> findAllByItemOwnerIdAndStartAfter(long bookerId, LocalDateTime dateTime, Pageable pageRequest);

    // FUTURE
    Page<Booking> findAllByItemOwnerIdAndEndBefore(long bookerId, LocalDateTime dateTime, Pageable pageRequest);

    // WAITING / REJECTED
    Page<Booking> findAllByItemOwnerIdAndStatusEquals(long bookerId, Status status, Pageable pageRequest);

    Optional<Booking> findFirstByItemIdInAndStartBeforeOrderByEndDesc(List<Long> idItems, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStartBeforeOrderByEndDesc(long itemId, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdInAndStartAfterAndStatusOrderByStartAsc(List<Long> idItems, LocalDateTime dateTime, Status status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(long itemId, LocalDateTime dateTime, Status status);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatusEquals(long bookerId, long itemId, LocalDateTime dateTime, Status status);
}
