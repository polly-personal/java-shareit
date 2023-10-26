package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerReview {
    private String text;
    private boolean itemIsWorked;
}
