package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * TODO Sprint add-controllers.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "поле \"name\" должно быть заполнено", groups = MainValidation.class)
    private String name;

    @NotBlank(message = "поле \"description\" должно быть заполнено", groups = MainValidation.class)
    private String description;

    @NotNull(message = "поле \"available\" должно быть заполнено", groups = MainValidation.class)
    private Boolean available;

    private Long requestId;
}
