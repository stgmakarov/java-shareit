package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingOutDto2;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingOutDto toBookingOutDto(Booking booking) {
        return new BookingOutDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingOutDto2 toBookingOutDto2(Booking booking) {
        if (booking == null) return null;
        return new BookingOutDto2(
                booking.getId(),
                booking.getBooker().getId()
        );
    }

    public static Booking toBooking(BookingInDto bookingInDto, User user, Item item) {
        return new Booking(0,
                bookingInDto.getStart(),
                bookingInDto.getEnd(),
                item,
                user,
                BookingStatus.WAITING
        );
    }
}
