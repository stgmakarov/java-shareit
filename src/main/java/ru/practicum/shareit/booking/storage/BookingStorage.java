package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.ReqStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingStorage {
    Booking create(Booking booking);

    Booking update(Booking booking);

    Booking getById(long bookingId);

    List<Booking> getByBookerId(long userId, boolean byOwner);

    List<Booking> getByBookerIdAndTime(long userId, ReqStatus status, boolean byOwner);

    List<Booking> getByBookerIdAndStatus(long userId, ReqStatus status, boolean byOwner);

    List<Booking> getByItem(Item item);

    List<Booking> getByItem(long itemId);

    List<Booking> getAllBookingsByItemList(List<Item> items);
}
