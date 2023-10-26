package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@RestController
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;


    @GetMapping("/{bookingId}")
    public BookingDtoOut getByIdAndRequestorId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long bookerId,
                                               @PathVariable Long bookingId) {
        return bookingService.getByIdAndRequestorId(bookerId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllByBookerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long bookerId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        userService.idIsExists(bookerId);
        return bookingService.getAllByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        userService.idIsExists(ownerId);
        return bookingService.getAllByOwnerId(ownerId, state);
    }

    @PostMapping
    public BookingDtoOut create(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
                                @Validated(CreateValidation.class) @RequestBody BookingDtoIn bookingDtoIn) {
        return bookingService.create(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut update(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
                                @PathVariable(name = "bookingId") @Positive @Min(1) Long bookingId,
                                @RequestParam(name = "approved") boolean isApproval) {
        return bookingService.updateStatus(ownerId, bookingId, isApproval);
    }
}
