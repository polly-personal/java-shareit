package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Builder
@Data
public class CommentDtoOut {
    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
