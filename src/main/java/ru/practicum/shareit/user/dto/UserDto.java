package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class UserDto {
    private Long id;

    @Email(message = "некорректный email")
    private String email;

    private String name;
}
