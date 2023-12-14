package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder
@Data
public class BookingDtoIn {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;
}
