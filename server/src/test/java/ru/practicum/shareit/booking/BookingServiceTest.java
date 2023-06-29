package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingDbStorage;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BookingServiceTest {
    BookingStorage bookingStorage;
    UserDbStorage userStorage;
    ItemServiceImpl itemService;
    ItemDbStorage itemStorage;

    BookingService bookingService;
    User owner;
    User booker;
    User user;
    Item item;
    Booking booking;

    @BeforeEach
    public void init() {
        bookingStorage = Mockito.mock(BookingDbStorage.class);
        userStorage = Mockito.mock(UserDbStorage.class);
        itemService = Mockito.mock(ItemServiceImpl.class);
        itemStorage = Mockito.mock(ItemDbStorage.class);

        itemService.setItemStorage(itemStorage);

        bookingService = new BookingServiceImpl(bookingStorage, userStorage, itemService);

        owner = new User(1L, "Owner name", "owner@mail.com");
        booker = new User(2L, "Booker name", "booker@mail.com");
        user = new User(3L, "User name", "user@mail.com");

        item = new Item(1L, "item", "description",
                true, owner, null);
        booking = new Booking(1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item,
                booker,
                BookingStatus.WAITING
        );
    }

    @Test
    public void createBookingTest() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(itemService.getById(anyLong())).thenReturn(item);
        when(itemService.isItemAvailable(anyLong())).thenReturn(true);
        when(bookingStorage.create(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Booking booking = bookingService.create(
                new BookingInDto(
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        item.getId()),
                user.getId());

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(booking.getItem().getId(), item.getId());
    }

    @Test
    public void createBookingFailTest() {
        when(userStorage.get(anyLong())).thenReturn(null);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(itemService.getById(anyLong())).thenReturn(item);
        when(itemService.isItemAvailable(anyLong())).thenReturn(true);
        when(bookingStorage.create(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking = bookingService.create(
                    new BookingInDto(
                            LocalDateTime.now().plusHours(1),
                            LocalDateTime.now().plusHours(2),
                            item.getId()),
                    user.getId());
        });
    }

    @Test
    public void createBookingFail2Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(null);
        when(itemService.getById(anyLong())).thenReturn(null);
        when(itemService.isItemAvailable(anyLong())).thenReturn(true);
        when(bookingStorage.create(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking = bookingService.create(
                    new BookingInDto(
                            LocalDateTime.now().plusHours(1),
                            LocalDateTime.now().plusHours(2),
                            item.getId()),
                    user.getId());
        });
    }

    @Test
    public void createBookingFail3Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(itemService.getById(anyLong())).thenReturn(item);
        when(itemService.isItemAvailable(anyLong())).thenReturn(false);
        when(bookingStorage.create(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking = bookingService.create(
                    new BookingInDto(
                            LocalDateTime.now().plusHours(1),
                            LocalDateTime.now().plusHours(2),
                            item.getId()),
                    user.getId());
        });
    }

    @Test
    public void createBookingFail4Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(itemService.getById(anyLong())).thenReturn(item);
        when(itemService.isItemAvailable(anyLong())).thenReturn(true);
        when(bookingStorage.create(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking = bookingService.create(
                    new BookingInDto(
                            LocalDateTime.now().plusHours(1),
                            LocalDateTime.now().plusHours(2),
                            item.getId()),
                    owner.getId());
        });
    }

    @Test
    public void createBookingFail5Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(itemService.getById(anyLong())).thenReturn(item);
        when(itemService.isItemAvailable(anyLong())).thenReturn(true);
        when(bookingStorage.create(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking = bookingService.create(
                    new BookingInDto(
                            LocalDateTime.now().plusHours(1),
                            LocalDateTime.now().minusHours(2),
                            item.getId()),
                    owner.getId());
        });
    }

    @Test
    public void setApproveTest() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(bookingStorage.getById(anyLong())).thenReturn(booking);

        when(bookingStorage.update(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Booking booking2 = bookingService.setApprove(booking.getId(), owner.getId(), true);

        Assertions.assertNotNull(booking2);
        Assertions.assertEquals(booking2.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    public void setApproveFail1Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(bookingStorage.getById(anyLong())).thenReturn(null);

        when(bookingStorage.update(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking2 = bookingService.setApprove(
                    booking.getId(),
                    owner.getId(),
                    true);
        });
    }

    @Test
    public void setApproveFail2Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(bookingStorage.getById(anyLong())).thenReturn(booking);

        when(bookingStorage.update(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking2 = bookingService.setApprove(
                    booking.getId(),
                    user.getId(),
                    true);
        });
    }

    @Test
    public void setApproveFail3Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingStorage.getById(anyLong())).thenReturn(booking);

        when(bookingStorage.update(any(Booking.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking2 = bookingService.setApprove(
                    booking.getId(),
                    owner.getId(),
                    true);
        });
    }

    @Test
    public void getBookingTest() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(bookingStorage.getById(anyLong())).thenReturn(booking);

        Booking booking2 = bookingService.getBooking(booking.getId(), owner.getId());

        Assertions.assertNotNull(booking2);
    }

    @Test
    public void getBookingFail1Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(bookingStorage.getById(anyLong())).thenReturn(null);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking2 = bookingService.getBooking(booking.getId(), owner.getId());
        });
    }

    @Test
    public void getBookingFail2Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(bookingStorage.getById(anyLong())).thenReturn(booking);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Booking booking2 = bookingService.getBooking(booking.getId(), user.getId());
        });
    }

    @Test
    public void getAllBookingTest() {
        List<Booking> bookingList = List.of(booking);
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(bookingStorage.getById(anyLong())).thenReturn(booking);

        List<Booking> bookingList1 = bookingService.getAllBooking(
                user.getId(), "ALL", false, 0L, 20L);
        Assertions.assertNotNull(bookingList);
        Assertions.assertEquals(bookingList.size(), 1);

        List<Booking> bookingList2 = bookingService.getAllBooking(
                user.getId(), "PAST", false, 0L, 20L);
        Assertions.assertNotNull(bookingList2);
        Assertions.assertEquals(bookingList2.size(), 0);

        List<Booking> bookingList3 = bookingService.getAllBooking(
                user.getId(), "REJECTED", false, 0L, 20L);
        Assertions.assertNotNull(bookingList2);
        Assertions.assertEquals(bookingList2.size(), 0);
    }

}
