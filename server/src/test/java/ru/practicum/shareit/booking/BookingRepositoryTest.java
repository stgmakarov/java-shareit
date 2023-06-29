package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.ShareitTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private User booker;
    private User owner;
    private Booking booking;

    @BeforeEach
    public void initialize() {
        owner = ShareitTestUtils.createEntity(new User("owner", "owner@mail.com"), userRepository);

        item = ShareitTestUtils.createEntity(new Item(0, "name", "desc", true,
                owner, null), itemRepository);

        booker = ShareitTestUtils.createEntity(new User("booker", "booker@mail.com"), userRepository);

        booking = ShareitTestUtils.createEntity(initBooking(item, booker, BookingStatus.WAITING), bookingRepository);
    }

    private Booking initBooking(Item item, User booker, BookingStatus status) {
        return new Booking(0, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item,
                booker,
                status
        );
    }

    @Test
    public void createBookingTest() {
        Assertions.assertNotNull(booking);
    }

    @Test
    public void findByItem_IdInTest() {
        List<Booking> bookings = bookingRepository.findByItem_IdIn(Collections.singletonList(item.getId()));
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getId(), booking.getId());
    }

    @Test
    public void findByBookerIdTest() {
        List<Booking> bookings = bookingRepository.findByBookerId(booker.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getId(), booking.getId());
    }

    @Test
    public void findByBooker_IdOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findByBooker_IdOrderByStartDesc(booker.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getId(), booking.getId());
    }

    @Test
    public void findByItem_Owner_IdOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findByItem_Owner_IdOrderByStartDesc(owner.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getId(), booking.getId());
    }

    @Test
    public void findAllWithItemTest() {
        Booking booking2 = ShareitTestUtils.createEntity(initBooking(item, booker, BookingStatus.WAITING), bookingRepository);

        List<Booking> bookings = bookingRepository.findAllWithItem(Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 2);
    }

    @Test
    public void findByItemTest() {
        List<Booking> bookings = bookingRepository.findByItem(item.getId());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getItem().getId(), item.getId());
    }

    @Test
    public void findByDateCurrentBookerTest() {
        List<Booking> bookings = bookingRepository.findByDateCurrentBooker(LocalDateTime.now().plusMinutes(90),
                booker.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getBooker().getId(), booker.getId());
    }

    @Test
    public void findByDateCurrentBookerFailTest() {
        List<Booking> bookings = bookingRepository.findByDateCurrentBooker(LocalDateTime.now(),
                booker.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 0);
    }

    @Test
    public void findByDateCurrentOwnerTest() {
        List<Booking> bookings = bookingRepository.findByDateCurrentOwner(LocalDateTime.now().plusMinutes(90),
                owner.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getItem().getOwner().getId(), owner.getId());
    }

    @Test
    public void findByDateCurrentOwnerFailTest() {
        List<Booking> bookings = bookingRepository.findByDateCurrentOwner(LocalDateTime.now(),
                owner.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 0);
    }

    @Test
    public void findByDateFutureBookerTest() {
        List<Booking> bookings = bookingRepository.findByDateFutureBooker(LocalDateTime.now(),
                booker.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getBooker().getId(), booker.getId());
    }

    @Test
    public void findByDateFutureOwnerTest() {
        List<Booking> bookings = bookingRepository.findByDateFutureOwner(LocalDateTime.now(),
                owner.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getItem().getOwner().getId(), owner.getId());
    }

    @Test
    public void findByDatePastBookerTest() {
        List<Booking> bookings = bookingRepository.findByDatePastBooker(LocalDateTime.now().plusMinutes(300),
                booker.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getBooker().getId(), booker.getId());
    }

    @Test
    public void findByDatePastOwnerTest() {
        List<Booking> bookings = bookingRepository.findByDatePastOwner(LocalDateTime.now().plusMinutes(300),
                owner.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getItem().getOwner().getId(), owner.getId());
    }

    @Test
    public void findByStatusBookerTest() {
        List<Booking> bookings = bookingRepository.findByStatusBooker(booking.getStatus(),
                booker.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getBooker().getId(), booker.getId());
    }

    @Test
    public void findByStatusOwnerTest() {
        List<Booking> bookings = bookingRepository.findByStatusOwner(booking.getStatus(),
                owner.getId(), Pageable.unpaged());
        Assertions.assertEquals(bookings.size(), 1);
        Assertions.assertEquals(bookings.get(0).getItem().getOwner().getId(), owner.getId());
    }

}
