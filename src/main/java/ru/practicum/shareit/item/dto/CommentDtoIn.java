package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class CommentDtoIn {
    private Long id;

    @NotBlank(message = "поле \"text\" должно быть заполнено", groups = CreateValidation.class)
    private String text;

    private String authorName;
}
