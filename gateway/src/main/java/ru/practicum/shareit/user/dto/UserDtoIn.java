package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class UserDtoIn {
    private Long id;

    @NotBlank(message = "поле \"name\" должно быть заполнено", groups = CreateValidation.class)
    private String name;

    @NotBlank(message = "поле \"email\" должно быть заполнено", groups = CreateValidation.class)
    @Email(message = "некорректное поле \"email\"", groups = {CreateValidation.class, UpdateValidation.class})
    private String email;
}
