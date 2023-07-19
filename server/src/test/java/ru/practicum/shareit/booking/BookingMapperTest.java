package ru.practicum.shareit.booking;

import org.junit.Test;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingOutDto2;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingMapperTest {
    @Test
    public void testToBookingOutDto() {
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), new Item(), new User(), BookingStatus.WAITING);
        BookingOutDto bookingOutDto = BookingMapper.toBookingOutDto(booking);
        assertNotNull(bookingOutDto);
        assertEquals(booking.getId(), bookingOutDto.getId());
        assertEquals(booking.getStart(), bookingOutDto.getStart());
        assertEquals(booking.getEnd(), bookingOutDto.getEnd());
        assertEquals(booking.getItem(), bookingOutDto.getItem());
        assertEquals(booking.getBooker(), bookingOutDto.getBooker());
        assertEquals(booking.getStatus(), bookingOutDto.getStatus());
    }

    @Test
    public void testToBookingOutDto2() {
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), new Item(), new User(), BookingStatus.WAITING);
        BookingOutDto2 bookingOutDto2 = BookingMapper.toBookingOutDto2(booking);
        assertNotNull(bookingOutDto2);
        assertEquals(booking.getId(), bookingOutDto2.getId());
        assertEquals(booking.getBooker().getId(), bookingOutDto2.getBookerId());
    }

    @Test
    public void testToBooking() {
        User user = new User();
        Item item = new Item();
        BookingInDto bookingInDto = new BookingInDto(LocalDateTime.now(), LocalDateTime.now().plusHours(1), 0L);
        Booking booking = BookingMapper.toBooking(bookingInDto, user, item);
        assertNotNull(booking);
        assertEquals(0L, booking.getId());
        assertEquals(user, booking.getBooker());
        assertEquals(item, booking.getItem());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }
}

