package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "поле \"description\" должно быть заполнено", groups = MainValidation.class)
    private String description;

    private Long requestor;

    private LocalDateTime created;
}
