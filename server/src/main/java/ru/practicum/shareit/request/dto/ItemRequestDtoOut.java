package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Data
public class ItemRequestDtoOut {
    private Long id;

    private String description;

    private LocalDateTime created;

    private Long requesterId;

    private List<ItemDtoOut> items;
}
