package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDtoIn {
    private Long id;

    private String text;

    private String authorName;
}
