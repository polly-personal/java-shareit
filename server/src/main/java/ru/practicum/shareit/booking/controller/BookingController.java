package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@RestController
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private static final String requestHeaderUserId = "X-Sharer-User-Id";


    @PostMapping
    public BookingDtoOut create(@RequestHeader(requestHeaderUserId) long userId, @RequestBody BookingDtoIn bookingDtoIn) {
        log.info("🟫 POST /bookings");
        return bookingService.create(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateStatus(@RequestHeader(requestHeaderUserId) long ownerId,
                                      @PathVariable(name = "bookingId") long bookingId,
                                      @RequestParam(name = "approved") boolean isApproval) {
        log.info("🟫 PATCH /bookings/{}?approved={}", bookingId, isApproval);
        return bookingService.updateStatus(ownerId, bookingId, isApproval);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getByIdAndOwnerOrBookerId(@RequestHeader(requestHeaderUserId) long ownerOrBookerId,
                                                   @PathVariable long bookingId) {
        log.info("🟫 GET /bookings/{}", bookingId);

        return bookingService.getByIdAndOwnerOrBookerId(ownerOrBookerId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllByBookerId(@RequestHeader(requestHeaderUserId) long bookerId,
                                                @RequestParam String state,
                                                @RequestParam int from,
                                                @RequestParam int size) {
        log.info("🟫 запрос от бронирующего: GET /bookings?state={}&from={}&size={}", state, from, size);
        userService.idIsExists(bookerId);
        return bookingService.getAllByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwnerId(@RequestHeader(requestHeaderUserId) long ownerId,
                                               @RequestParam String state,
                                               @RequestParam int from,
                                               @RequestParam int size) {
        log.info("🟫 запрос от владельца: GET /bookings?state={}&from={}&size={}", state, from, size);
        userService.idIsExists(ownerId);
        return bookingService.getAllByOwnerId(ownerId, state, from, size);
    }
}
