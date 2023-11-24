package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoOutForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class BookingMapper {
    public BookingDtoOut toBookingDto(Booking booking) {
        Item item = booking.getItem();
        BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(new BookingDtoOut.Booker(booking.getBooker().getId()))
                .item(new BookingDtoOut.Item(item.getId(), item.getName()))
                .build();

        log.info("🔀 booking: " + booking + " сконвертирован в bookingDtoOut: " + bookingDtoOut);
        return bookingDtoOut;
    }

    public List<BookingDtoOut> toBookingsDto(List<Booking> bookings) {
        List<BookingDtoOut> bookingDtoOuts = bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        log.info("🔀 список bookings: " + bookings + " сконвертирован в bookingDtoOuts: " + bookingDtoOuts);
        return bookingDtoOuts;
    }

    public BookingDtoIn toBookingDtoIn(Booking booking) {
        Item item = booking.getItem();
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(item.getId())
                .build();

        log.info("🔀 booking: " + booking + " сконвертирован в bookingDtoIn: " + bookingDtoIn);
        return bookingDtoIn;
    }


    public BookingDtoOutForItem toBookingDtoForItem(Booking booking) {
        BookingDtoOutForItem bookingDtoOutForItem = BookingDtoOutForItem.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .build();

        log.info("🔀 booking: " + booking + " сконвертирован в bookingDtoOutForItem: " + bookingDtoOutForItem);
        return bookingDtoOutForItem;
    }


    public Booking toBooking(BookingDtoIn bookingDtoIn) {
        Booking booking = Booking.builder()
                .id(bookingDtoIn.getId())
                .start(bookingDtoIn.getStart())
                .end(bookingDtoIn.getEnd())
                .build();

        log.info("🔀 giveBookingDto: " + bookingDtoIn + " сконвертирован в booking: " + booking);
        return booking;
    }

    public List<Booking> toBookings(List<BookingDtoIn> bookingsDtoIn) {
        List<Booking> bookings = bookingsDtoIn
                .stream()
                .map(BookingMapper::toBooking)
                .collect(Collectors.toList());

        log.info("🔀 список bookingsDtoIn: " + bookingsDtoIn + " сконвертирован в bookings: " + bookings);
        return bookings;
    }
}
