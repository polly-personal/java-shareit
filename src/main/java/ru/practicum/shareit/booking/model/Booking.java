package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.constant.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
@Data
public class Booking {
    private Long id;

    @NotNull(message = "поле \"start\" должно быть заполнено")
    private LocalDateTime start;

    @NotNull(message = "поле \"end\" должно быть заполнено")
    private LocalDateTime end;

    @NotNull(message = "поле \"itemId\" должно быть заполнено")
    private Long itemId;

    private Long booker;

    private Status status;

    private Long ownerItem;

    private CustomerReview customerReview;
}
