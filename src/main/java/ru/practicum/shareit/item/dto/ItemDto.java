package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private final Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Long requestId;
}
