package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class GiveCommentDto {
    private Long id;

    @NotBlank(message = "поле \"text\" должно быть заполнено", groups = CreateValidation.class)
    private String text;

    @NotBlank(message = "поле \"authorName\" должно быть заполнено", groups = CreateValidation.class)
    private String authorName;

//    private LocalDateTime created;
}
