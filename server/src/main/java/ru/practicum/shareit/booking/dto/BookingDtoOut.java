package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.constant.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@Data
public class BookingDtoOut {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;

    private Booker booker;

    private Item item;

    @AllArgsConstructor
    @Data
    public static class Booker {
        private Long id;
    }

    @AllArgsConstructor
    @Data
    public static class Item {
        private Long id;
        private String name;
    }
}
