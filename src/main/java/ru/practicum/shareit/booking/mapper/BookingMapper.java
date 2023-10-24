package ru.practicum.shareit.booking.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.GetBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.GiveBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BookingMapper {
    public static GetBookingDto toBookingDto(Booking booking) {
        GetBookingDto getBookingDto = GetBookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
//                booking.getOwnerItem(),
//                booking.getCustomerReview()
                .build();
        log.info("🔀 booking: " + booking + " сконвертирован в getBookingDto: " + getBookingDto);

        return getBookingDto;
    }

    public static List<GetBookingDto> toBookingsDto(List<Booking> bookings) {
        List<GetBookingDto> getBookingDtos = bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        log.info("🔀 список bookings: " + bookings + " сконвертирован в getBookingDtos: " + getBookingDtos);

        return getBookingDtos;
    }


    public static Booking toBooking(GiveBookingDto giveBookingDto) {
        Booking booking = Booking.builder()
                .id(giveBookingDto.getId())
                .start(giveBookingDto.getStart())
                .end(giveBookingDto.getEnd())
//                .item()giveBookingDto.getItemId()
//                .booker()giveBookingDto.getBooker()
//                .status(giveBookingDto.getStatus())
//                giveBookingDto.getOwnerItem(),
//                giveBookingDto.getCustomerReview()
                .build();
        log.info("🔀 giveBookingDto: " + giveBookingDto + " сконвертирован в booking: " + booking);

        return booking;
    }

    public static List<Booking> toBookings(List<GiveBookingDto> giveBookingsDto) {
        List<Booking> bookings = giveBookingsDto
                .stream()
                .map(BookingMapper::toBooking)
                .collect(Collectors.toList());

        log.info("🔀 список giveBookingsDto: " + giveBookingsDto + " сконвертирован в bookings: " + bookings);

        return bookings;
    }
}
