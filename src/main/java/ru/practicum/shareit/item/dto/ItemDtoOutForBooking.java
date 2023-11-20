package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * TODO Sprint add-controllers.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class ItemDtoOutForBooking {
    private Long id;

    @NotBlank(message = "поле \"name\" должно быть заполнено", groups = CreateValidation.class)
    private String name;

    @NotBlank(message = "поле \"description\" должно быть заполнено", groups = CreateValidation.class)
    private String description;

    @NotNull(message = "поле \"available\" должно быть заполнено", groups = CreateValidation.class)
    private Boolean available;

    private User owner;

    private ItemRequest itemRequest;
}
