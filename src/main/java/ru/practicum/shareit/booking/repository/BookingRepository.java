package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDtoOutForItem;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStatusEquals(Long bookerId, Status status, Sort sort);

    List<Booking> findAllByItemOwnerId(Long bookerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatusEquals(Long bookerId, Status status, Sort sort);

    BookingDtoOutForItem findFirstByItemIdAndStartBeforeOrderByEndDesc(Long itemId, LocalDateTime dateTime);

    BookingDtoOutForItem findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime dateTime, Status status);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatusEquals(Long bookerId, Long itemId, LocalDateTime dateTime, Status status);
}
