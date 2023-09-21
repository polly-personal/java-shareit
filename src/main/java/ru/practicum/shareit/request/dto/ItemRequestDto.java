package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Data
public class ItemRequestDto {
    private Long id;

    @NotBlank
    private String description;

    private Long requestor;

    @PastOrPresent(message = "поле \"created\" не может быть в прошлом")
    private LocalDateTime created;
}
