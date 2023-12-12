package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemRequestDtoIn {
    @NotBlank(message = "поле \"description\" должно быть заполнено", groups = CreateValidation.class)
    private String description;
}