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


    @GetMapping("/{bookingId}")
    public BookingDtoOut getByIdAndRequestorId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long requestorId,
                                               @PathVariable long bookingId) {
        log.info("🟫 GET /bookings/{}", bookingId);

        return bookingService.getByIdAndRequestorId(requestorId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllByBookerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long bookerId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        log.info("🟫 запрос от бронирующего: GET /bookings?state={}", state);
        userService.idIsExists(bookerId);
        return bookingService.getAllByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) long ownerId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        log.info("🟫 запрос от владельца: GET /bookings?state={}", state);
        userService.idIsExists(ownerId);
        return bookingService.getAllByOwnerId(ownerId, state);
    }

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
}
