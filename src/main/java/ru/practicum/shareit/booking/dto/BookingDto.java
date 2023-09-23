package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.constant.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
public class BookingDto {
    private Long id;

    @FutureOrPresent(message = "поле \"start\" должно быть сейчас или в будущем", groups = MainValidation.class)
    @NotNull(message = "поле \"start\" должно быть заполнено", groups = MainValidation.class)
    private LocalDateTime start;

    @Future(message = "поле \"end\" должно быть в будущем", groups = MainValidation.class)
    @NotNull(message = "поле \"end\" должно быть заполнено", groups = MainValidation.class)
    private LocalDateTime end;

    @NotNull(message = "поле \"itemId\" должно быть заполнено", groups = MainValidation.class)
    private Long itemId;

    private Long booker;

    private Status status;

    private Long ownerItem;

    private CustomerReview customerReview;
}
