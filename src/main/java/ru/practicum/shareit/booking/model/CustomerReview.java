package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
//@AllArgsConstructor
@Data
public class CustomerReview {
    private String text;
    private boolean itemIsWorked;
}
