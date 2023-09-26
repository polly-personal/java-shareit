package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "поле \"email\" должно быть заполнено", groups = CreateValidation.class)
    @Email(message = "некорректное поле \"email\"", groups = {CreateValidation.class, UpdateValidation.class})
    private String email;

    @NotBlank(message = "поле \"name\" должно быть заполнено", groups = CreateValidation.class)
    private String name;
}
