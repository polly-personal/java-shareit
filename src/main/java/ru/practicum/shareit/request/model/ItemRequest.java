package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Data
public class ItemRequest {
    private Long id;

    private String description;

    private Long requestor;

    private LocalDateTime created;
}
