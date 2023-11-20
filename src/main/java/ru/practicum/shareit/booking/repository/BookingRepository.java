package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndStartAfter(long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStatusEquals(long bookerId, Status status, Sort sort);

    List<Booking> findAllByItemOwnerId(long bookerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartAfter(long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndBefore(long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatusEquals(long bookerId, Status status, Sort sort);

    Optional<Booking> findFirstByItemIdInAndStartBeforeOrderByEndDesc(List<Long> idItems, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStartBeforeOrderByEndDesc(long itemId, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdInAndStartAfterAndStatusOrderByStartAsc(List<Long> idItems, LocalDateTime dateTime, Status status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(long itemId, LocalDateTime dateTime, Status status);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatusEquals(long bookerId, long itemId, LocalDateTime dateTime, Status status);
}
