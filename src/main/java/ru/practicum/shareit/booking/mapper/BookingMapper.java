package ru.practicum.shareit.booking.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BookingMapper {
    public static BookingDtoOut toBookingDto(Booking booking) {
        Item item = booking.getItem();
        BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(new BookingDtoOut.Booker(booking.getBooker().getId()))
                .item(new BookingDtoOut.Item(item.getId(), item.getName()))
                .build();

        log.info("🔀 booking: " + booking + " сконвертирован в getBookingDto: " + bookingDtoOut);
        return bookingDtoOut;
    }

    public static List<BookingDtoOut> toBookingsDto(List<Booking> bookings) {
        List<BookingDtoOut> bookingDtoOuts = bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        log.info("🔀 список bookings: " + bookings + " сконвертирован в getBookingDtos: " + bookingDtoOuts);
        return bookingDtoOuts;
    }


    public static Booking toBooking(BookingDtoIn bookingDtoIn) {
        Booking booking = Booking.builder()
                .id(bookingDtoIn.getId())
                .start(bookingDtoIn.getStart())
                .end(bookingDtoIn.getEnd())
                .build();

        log.info("🔀 giveBookingDto: " + bookingDtoIn + " сконвертирован в booking: " + booking);
        return booking;
    }

    public static List<Booking> toBookings(List<BookingDtoIn> giveBookingsDto) {
        List<Booking> bookings = giveBookingsDto
                .stream()
                .map(BookingMapper::toBooking)
                .collect(Collectors.toList());

        log.info("🔀 список giveBookingsDto: " + giveBookingsDto + " сконвертирован в bookings: " + bookings);
        return bookings;
    }
}
