package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.MainValidation;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.CustomerReview;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-bookings.
 */
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@RestController
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
                             @Validated(MainValidation.class) @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveOrRejected(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long ownerId,
                                        @PathVariable(name = "bookingId") @Positive @Min(1) Long bookingId,
                                        @RequestParam boolean approved) {
        if (approved == true) {
            return bookingService.approve(ownerId, bookingId);
        } else {
            return bookingService.reject(ownerId, bookingId);
        }
    }

    @DeleteMapping("/{bookingId}")
    public BookingDto cancel(@RequestHeader("X-Sharer-User-Id") @Positive @Min(1) Long userId,
                             @PathVariable(name = "bookingId") @Positive @Min(1) Long bookingId) {
        return bookingService.cancel(userId, bookingId);
    }

    @PostMapping("/review/{bookingId}")
    public BookingDto createCustomerReview(@PathVariable(name = "bookingId") @Positive @Min(1) Long bookingId,
                                           @RequestBody @NotNull CustomerReview customerReview) {
        return bookingService.createCustomerReview(bookingId, customerReview);
    }
}
