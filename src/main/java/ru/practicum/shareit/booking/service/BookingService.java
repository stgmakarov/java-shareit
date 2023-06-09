package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utilites.ParamNotFoundException;

import java.util.List;

public interface BookingService {
    Booking create(BookingInDto bookingInDto, long userId);

    Booking setApprove(long bookingId, long userId, boolean approved);

    Booking getBooking(long bookingId, long userId);

    List<Booking> getAllBooking(long userId, String status, boolean byOwner) throws ParamNotFoundException;

}
