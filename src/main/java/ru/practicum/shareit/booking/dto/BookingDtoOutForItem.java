package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class BookingDtoOutForItem {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
}
