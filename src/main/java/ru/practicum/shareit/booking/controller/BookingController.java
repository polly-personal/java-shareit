package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.CreateValidation;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@RestController
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;


    @PostMapping
    public BookingDtoOut create(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long userId,
                                @Validated(CreateValidation.class) @RequestBody BookingDtoIn bookingDtoIn) {
        log.info("🟫 POST /bookings");
        return bookingService.create(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut update(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long ownerId,
                                @PathVariable(name = "bookingId") @Positive @Min(1) long bookingId,
                                @RequestParam(name = "approved") boolean isApproval) {
        log.info("🟫 PATCH /bookings/{}?approved={}", bookingId, isApproval);
        return bookingService.updateStatus(ownerId, bookingId, isApproval);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getByIdAndOwnerOrBookerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long ownerOrBookerId,
                                                   @PathVariable long bookingId) {
        log.info("🟫 GET /bookings/{}", bookingId);

        return bookingService.getByIdAndOwnerOrBookerId(ownerOrBookerId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllByBookerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long bookerId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive @Min(1) int size) {
        log.info("🟫 запрос от бронирующего: GET /bookings?state={}&from={}&size={}", state, from, size);
        return bookingService.getAllByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long ownerId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive @Min(1) int size) {
        log.info("🟫 запрос от владельца: GET /bookings?state={}&from={}&size={}", state, from, size);
        userService.idIsExists(ownerId);
        return bookingService.getAllByOwnerId(ownerId, state, from, size);
    }
}
