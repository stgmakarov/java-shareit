package ru.practicum.shareit.booking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingDbStorage;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookingDbStorageTest {
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingDbStorage bookingDbStorage;

    @Test
    public void testCreate() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(new User(1L, "name", "user@mail.com"));

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingDbStorage.create(booking);

        assertEquals(result.getId(), booking.getId());
        assertEquals(result.getBooker().getId(), booking.getBooker().getId());
    }

    @Test
    public void testUpdate() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(new User(1L, "name", "user@mail.com"));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        Booking createdBooking = bookingDbStorage.create(booking);
        createdBooking.setStatus(BookingStatus.APPROVED);
        Booking updatedBooking = bookingDbStorage.update(createdBooking);
        assertEquals(createdBooking.getStatus(), updatedBooking.getStatus());
    }

    @Test
    public void testGetById() {
        long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setId(bookingId);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Booking foundBooking = bookingDbStorage.getById(bookingId);
        assertEquals(foundBooking.getId(), booking.getId());
    }

    @Test
    public void testGetByBookerId() {
        long userId = 1L;
        boolean byOwner = true;
        Pageable pageable = PageRequest.of(0, 20);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        when(bookingRepository.findByItem_Owner_IdOrderByStartDesc(anyLong(), any(Pageable.class)))
                .thenReturn(bookings);
        List<Booking> foundBookings = bookingDbStorage.getByBookerId(userId, byOwner, pageable);
        assertEquals(foundBookings.size(), bookings.size());
    }

    @Test
    public void testGetByBookerIdAndTime() {
        long userId = 1L;
        Pageable pageable = PageRequest.of(0, 20);

        Booking booking = new Booking();
        booking.setId(10L);

        List<Booking> expected = Collections.singletonList(booking);
        when(bookingRepository.findByDateCurrentBooker(any(LocalDateTime.class), anyLong(), any(Pageable.class)))
                .thenReturn(expected);
        when(bookingRepository.findByDateCurrentOwner(any(LocalDateTime.class), anyLong(), any(Pageable.class)))
                .thenReturn(expected);

        when(bookingRepository.findByDateFutureBooker(any(LocalDateTime.class), anyLong(), any(Pageable.class)))
                .thenReturn(expected);
        when(bookingRepository.findByDateFutureOwner(any(LocalDateTime.class), anyLong(), any(Pageable.class)))
                .thenReturn(expected);

        when(bookingRepository.findByDatePastBooker(any(LocalDateTime.class), anyLong(), any(Pageable.class)))
                .thenReturn(expected);
        when(bookingRepository.findByDatePastOwner(any(LocalDateTime.class), anyLong(), any(Pageable.class)))
                .thenReturn(expected);

        List<Booking> actual1 = bookingDbStorage.getByBookerIdAndTime(userId, ReqStatus.CURRENT, false, pageable);
        assertEquals(expected, actual1);
        List<Booking> actual2 = bookingDbStorage.getByBookerIdAndTime(userId, ReqStatus.CURRENT, true, pageable);
        assertEquals(expected, actual2);

        List<Booking> fut1 = bookingDbStorage.getByBookerIdAndTime(userId, ReqStatus.FUTURE, false, pageable);
        assertEquals(expected, fut1);
        List<Booking> fut2 = bookingDbStorage.getByBookerIdAndTime(userId, ReqStatus.FUTURE, true, pageable);
        assertEquals(expected, fut2);

        List<Booking> pas1 = bookingDbStorage.getByBookerIdAndTime(userId, ReqStatus.PAST, false, pageable);
        assertEquals(expected, pas1);
        List<Booking> pas2 = bookingDbStorage.getByBookerIdAndTime(userId, ReqStatus.PAST, true, pageable);
        assertEquals(expected, pas2);
    }

    @Test
    public void testGetByBookerIdAndStatus() {
        long userId = 1L;
        boolean byOwner = false;
        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        User booker = new User(1L, "booker", "booker@mail.com");
        booking1.setId(1L);
        booking1.setBooker(booker);
        booking1.setStatus(BookingStatus.WAITING);
        bookings.add(booking1);

        when(bookingRepository.findByStatusBooker(any(), anyLong(), any(Pageable.class))).thenReturn(bookings);

        List<Booking> result = bookingDbStorage.getByBookerIdAndStatus(userId, ReqStatus.WAITING, byOwner, pageable);
        assertEquals(bookings.size(), result.size());

        List<Booking> result2 = bookingDbStorage.getByBookerIdAndStatus(userId, ReqStatus.WAITING, !byOwner, pageable);
        assertEquals(0, result2.size());
    }

    @Test
    public void testGetByItem() {
        long itemId = 1L;

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        Item item = new Item();
        item.setId(itemId);
        booking1.setItem(item);
        bookings.add(booking1);

        when(bookingRepository.findAllWithItem(any(Pageable.class))).thenReturn(bookings);
        List<Booking> result = bookingDbStorage.getByItem(itemId);

        assertEquals(bookings.size(), result.size());
    }

    @Test
    public void testGetByItem2() {
        long itemId = 1L;

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        Item item = new Item();
        item.setId(itemId);
        booking1.setItem(item);
        bookings.add(booking1);

        when(bookingRepository.findAllWithItem(any(Pageable.class))).thenReturn(bookings);
        List<Booking> result = bookingDbStorage.getByItem(item);

        assertEquals(bookings.size(), result.size());
    }

    @Test
    public void testGetAllBookingsByItemList() {
        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        items.add(item1);

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        Item item2 = new Item();
        item2.setId(1L);
        booking1.setItem(item2);
        bookings.add(booking1);

        when(bookingRepository.findByItem_IdIn(anyList())).thenReturn(bookings);

        List<Booking> result = bookingDbStorage.getAllBookingsByItemList(items);

        assertEquals(bookings.size(), result.size());
    }


}

