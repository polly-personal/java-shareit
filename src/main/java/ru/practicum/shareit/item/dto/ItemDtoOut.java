package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoOutForItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
public class ItemDtoOut {
    private Long id;

    @NotBlank(message = "поле \"name\" должно быть заполнено", groups = CreateValidation.class)
    private String name;

    @NotBlank(message = "поле \"description\" должно быть заполнено", groups = CreateValidation.class)
    private String description;

    @NotNull(message = "поле \"available\" должно быть заполнено", groups = CreateValidation.class)
    private Boolean available;

    private BookingDtoOutForItem lastBooking;

    private BookingDtoOutForItem nextBooking;

    private List<CommentDtoOut> comments;

    private Long itemRequestId;
}
