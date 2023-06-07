package ru.practicum.shareit.booking.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.ReqStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingDbStorage {
    @Autowired
    private BookingRepository bookingRepository;

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking update(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getById(long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public List<Booking> getByBookerId(long userId, boolean byOwner) {
        if (!byOwner) return bookingRepository.findByBookerId(userId);
        else {
            return bookingRepository.findAllWithItem().stream()
                    .filter(b -> b.getItem().getOwner().getId() == userId)
                    .collect(Collectors.toList());
        }
    }

    public List<Booking> getByBookerIdAndTime(long userId, ReqStatus status, boolean byOwner) {
        List<Booking> ret;
        switch (status) {
            case CURRENT:
                if (!byOwner)
                    ret = bookingRepository.findByDateCurrentBooker(LocalDateTime.now(), userId);
                else
                    ret = bookingRepository.findByDateCurrentOwner(LocalDateTime.now(), userId);
                break;
            case FUTURE:
                if (!byOwner)
                    ret = bookingRepository.findByDateFutureBooker(LocalDateTime.now(), userId);
                else
                    ret = bookingRepository.findByDateFutureOwner(LocalDateTime.now(), userId);
                break;
            case PAST:
                if (!byOwner)
                    ret = bookingRepository.findByDatePastBooker(LocalDateTime.now(), userId);
                else
                    ret = bookingRepository.findByDatePastOwner(LocalDateTime.now(), userId);
                break;
            default:
                throw new RuntimeException("Некорректный параметр");
        }
        return ret;
    }

    public List<Booking> getByBookerIdAndStatus(long userId, ReqStatus status, boolean byOwner) {
        BookingStatus bookingStatus;
        bookingStatus = BookingStatus.valueOf(status.toString());
        if (!byOwner)
            return bookingRepository.findByStatusBooker(bookingStatus, userId);
        else
            return bookingRepository.findByStatusOwner(bookingStatus, userId);
    }

    public List<Booking> getByItem(Item item) {
        return getByItem(item.getId());
    }

    public List<Booking> getByItem(long itemId) {
        return bookingRepository.findAllWithItem().stream()
                .filter(b -> b.getItem().getId() == itemId)
                .collect(Collectors.toList());
    }
}
