package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.constant.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
//@AllArgsConstructor
@Data
public class GetBookingDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;

    private Long bookerId;

    private Long itemId;

    private String itemName;

//    private Long ownerItem;

//    private CustomerReview customerReview;
}
