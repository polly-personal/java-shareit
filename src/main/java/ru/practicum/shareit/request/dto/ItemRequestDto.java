package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "поле \"description\" должно быть заполнено", groups = CreateValidation.class)
    private String description;

    private Long requestor;

    private LocalDateTime created;
}
