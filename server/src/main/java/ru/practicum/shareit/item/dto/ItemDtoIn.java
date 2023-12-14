package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
public class ItemDtoIn {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
}
