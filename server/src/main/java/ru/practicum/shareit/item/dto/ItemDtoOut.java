package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoOutForItem;

import java.util.List;


/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
public class ItemDtoOut {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDtoOutForItem lastBooking;

    private BookingDtoOutForItem nextBooking;

    private List<CommentDtoOut> comments;

    private Long requestId;
}
