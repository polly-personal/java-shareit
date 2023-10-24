package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
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
@Builder
//@AllArgsConstructor
@Data
public class GiveBookingDto {
    private Long id;

    @FutureOrPresent(message = "поле \"start\" должно быть сейчас или в будущем", groups = CreateValidation.class)
    @NotNull(message = "поле \"start\" должно быть заполнено", groups = CreateValidation.class)
    private LocalDateTime start;

    @Future(message = "поле \"end\" должно быть в будущем", groups = CreateValidation.class)
    @NotNull(message = "поле \"end\" должно быть заполнено", groups = CreateValidation.class)
    private LocalDateTime end;

    @NotNull(message = "поле \"itemId\" должно быть заполнено", groups = CreateValidation.class)
    private Long itemId;

//    private Long booker;

//    private Status status;

//    private Long ownerItem;

//    private CustomerReview customerReview;
}
