package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.CreateValidation;
import ru.practicum.shareit.booking.constant.State;
import ru.practicum.shareit.booking.exception.BookingUnsupportedState;
import ru.practicum.shareit.constant.RequestHeaders;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Controller
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(RequestHeaders.userId) @Min(1) long userId,
                                         @RequestBody @Validated(CreateValidation.class) BookingDtoIn bookingDtoIn) {
        log.info("🟫🟫 POST /bookings");
        return bookingClient.create(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(RequestHeaders.userId) @Min(1) long ownerId,
                                               @PathVariable(name = "bookingId") @Min(1) long bookingId,
                                               @RequestParam(name = "approved") boolean isApproval) {
        log.info("🟫🟫 PATCH /bookings/{}?approved={}", bookingId, isApproval);
        return bookingClient.updateStatus(ownerId, bookingId, isApproval);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getByIdAndOwnerOrBookerId(@RequestHeader(RequestHeaders.userId) @Min(1) long ownerOrBookerId,
                                                            @PathVariable long bookingId) {
        log.info("🟫🟫 GET /bookings/{}", bookingId);
        return bookingClient.getByIdAndOwnerOrBookerId(ownerOrBookerId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookerId(@RequestHeader(RequestHeaders.userId) @Min(1) long bookerId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("🟫🟫 запрос от бронирующего: GET /bookings?state={}&from={}&size={}", stateParam, from, size);

        State state = State.from(stateParam)
                .orElseThrow(() -> new BookingUnsupportedState("Unknown state: UNSUPPORTED_STATUS"));
        return bookingClient.getAllByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwnerId(@RequestHeader(RequestHeaders.userId) @Min(1) long ownerId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("🟫🟫 запрос от владельца: GET /bookings?state={}&from={}&size={}", stateParam, from, size);

        State state = State.from(stateParam)
                .orElseThrow(() -> new BookingUnsupportedState("Unknown state: UNSUPPORTED_STATUS"));
        return bookingClient.getAllByOwnerId(ownerId, state, from, size);
    }
}
