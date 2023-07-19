package ru.practicum.shareit.booking.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.ReqStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingDbStorage implements BookingStorage {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Override
    public List<Booking> getByBookerId(long userId, boolean byOwner, Pageable pageable) {
        if (!byOwner) return bookingRepository.findByBooker_IdOrderByStartDesc(userId, pageable);
        else return bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId, pageable);
    }

    @Override
    public List<Booking> getByBookerIdAndTime(long userId, ReqStatus status, boolean byOwner, Pageable pageable) {
        List<Booking> ret = new ArrayList<>();
        switch (status) {
            case CURRENT:
                if (!byOwner) ret = bookingRepository.findByDateCurrentBooker(LocalDateTime.now(), userId, pageable);
                else ret = bookingRepository.findByDateCurrentOwner(LocalDateTime.now(), userId, pageable);
                break;
            case FUTURE:
                if (!byOwner) ret = bookingRepository.findByDateFutureBooker(LocalDateTime.now(), userId, pageable);
                else ret = bookingRepository.findByDateFutureOwner(LocalDateTime.now(), userId, pageable);
                break;
            case PAST:
                if (!byOwner) ret = bookingRepository.findByDatePastBooker(LocalDateTime.now(), userId, pageable);
                else ret = bookingRepository.findByDatePastOwner(LocalDateTime.now(), userId, pageable);
                break;
        }
        return ret;
    }

    @Override
    public List<Booking> getByBookerIdAndStatus(long userId, ReqStatus status, boolean byOwner, Pageable pageable) {
        BookingStatus bookingStatus;
        bookingStatus = BookingStatus.valueOf(status.toString());
        if (!byOwner) return bookingRepository.findByStatusBooker(bookingStatus, userId, pageable);
        else return bookingRepository.findByStatusOwner(bookingStatus, userId, pageable);
    }

    @Override
    public List<Booking> getByItem(Item item) {
        return getByItem(item.getId());
    }

    @Override
    public List<Booking> getByItem(long itemId) {
        return bookingRepository.findAllWithItem(Pageable.unpaged()).stream()
                .filter(b -> b.getItem().getId() == itemId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getAllBookingsByItemList(List<Item> items) {
        return bookingRepository.findByItem_IdIn(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
    }
}
