package ru.practicum.shareit.booking.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBooker(),
                booking.getStatus(),
                booking.getOwnerItem(),
                booking.getCustomerReview()
        );
        log.info("🔀 booking: " + booking + " сконвертирован в bookingDto: " + bookingDto);

        return bookingDto;
    }

    public static List<BookingDto> toBookingsDto(List<Booking> bookings) {
        List<BookingDto> bookingsDto = bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        log.info("🔀 список bookings: " + bookings + " сконвертирован в bookingsDto: " + bookingsDto);

        return bookingsDto;
    }


    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBooker(),
                bookingDto.getStatus(),
                bookingDto.getOwnerItem(),
                bookingDto.getCustomerReview()
        );
        log.info("🔀 bookingDto: " + bookingDto + " сконвертирован в booking: " + booking);

        return booking;
    }

    public static List<Booking> toBookings(List<BookingDto> bookingsDto) {
        List<Booking> bookings = bookingsDto
                .stream()
                .map(BookingMapper::toBooking)
                .collect(Collectors.toList());

        log.info("🔀 список bookingsDto: " + bookingsDto + " сконвертирован в bookings: " + bookings);

        return bookings;
    }
}
