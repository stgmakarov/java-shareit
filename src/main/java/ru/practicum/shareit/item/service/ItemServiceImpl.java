package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.ReqStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingDbStorage;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentDbStorage;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemOutDto2;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utilites.ShareitLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Comparator.comparing;

/**
 * @author Stanislav Makarov
 */
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private BookingDbStorage bookingStorage;
    @Autowired
    private CommentDbStorage commentStorage;

    @Override
    public Item create(Item item, long userId) {
        if (userId <= 0) ShareitLogger.returnErrorMsg(HttpStatus.INTERNAL_SERVER_ERROR,
                "Не указан пользователь");
        if (isNullOrBlank(item.getName()))
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не задано название вещи");
        if (isNullOrBlank(item.getDescription()))
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не задано описание вещи");
        if (item.getAvailable() == null)
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не указана доступность вещи");

        item.setOwner(userStorage.get(userId));
        return itemStorage.create(item);
    }

    @Override
    public Item update(long itemId, Item item, long userId) {
        Item existItem = itemStorage.getById(itemId);
        if (existItem == null)
            ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID вещи %d не найден", itemId));
        if (existItem.getOwner().getId() != userId)
            ShareitLogger.returnErrorMsg(HttpStatus.FORBIDDEN, "Редактировать вещь может только её владелец.");
        return itemStorage.update(itemId, item);
    }

    @Override
    public Item getById(long itemId) {
        Item existItem = itemStorage.getById(itemId);
        if (existItem == null)
            ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID вещи %d не найден", itemId));

        return existItem;
    }

    @Override
    public ItemOutDto2 getByIdDto2(long itemId, long userId) {
        Item item = getById(itemId);
        List<Comment> comments = commentStorage.getCommentByItem(itemId);
        ItemOutDto2 itemOutDto2 = ItemMapper.toItemOutDto2(item, comments);
        if (item.getOwner().getId() == userId) {
            List<Booking> bookings = bookingStorage.getByItem(itemId);
            bookings = bookings.stream().filter(b -> {
                BookingStatus status = b.getStatus();
                return status != BookingStatus.CANCELED &&
                        status != BookingStatus.REJECTED;
            }).collect(Collectors.toList());
/*            if(bookings.size()>0){
                itemOutDto2.setLastBooking(BookingMapper.toBookingOutDto2(bookings.get(0)));
                if(bookings.size()>1){
                    itemOutDto2.setNextBooking(BookingMapper.toBookingOutDto2(bookings.get(1)));
                }
            }*/
            itemOutDto2.setLastBooking(BookingMapper.toBookingOutDto2(getLastItem(bookings)));
            itemOutDto2.setNextBooking(BookingMapper.toBookingOutDto2(getNextItem(bookings)));
        }
        return itemOutDto2;
    }

    @Override
    public List<ItemOutDto2> getAllUserItems(long userId) {
        List<Item> items = itemStorage.getAllUserItems(userId);
        List<ItemOutDto2> outDto2List = new ArrayList<>();
        for (Item item : items) {
            List<Comment> comments = commentStorage.getCommentByItem(item.getId());
            ItemOutDto2 itemOutDto2 = ItemMapper.toItemOutDto2(item, comments);
            List<Booking> bookings = bookingStorage.getByItem(item.getId());
            bookings = bookings.stream().filter(b -> {
                BookingStatus status = b.getStatus();
                return status != BookingStatus.CANCELED &&
                        status != BookingStatus.REJECTED;
            }).collect(Collectors.toList());
            itemOutDto2.setLastBooking(BookingMapper.toBookingOutDto2(getLastItem(bookings)));
            itemOutDto2.setNextBooking(BookingMapper.toBookingOutDto2(getNextItem(bookings)));
/*            if(bookings.size()>0){
                itemOutDto2.setLastBooking(BookingMapper.toBookingOutDto2(bookings.get(0)));
                if(bookings.size()>1){
                    itemOutDto2.setNextBooking(BookingMapper.toBookingOutDto2(bookings.get(1)));
                }
            }*/
            outDto2List.add(itemOutDto2);
        }
        return outDto2List;
    }

    @Override
    public List<Item> findByText(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemStorage.findByText(text);
    }

    @Override
    public boolean isItemAvailable(long itemId) {
        return itemStorage.isItemAvailable(itemId);
    }

    @Override
    public List<Comment> getItemComment(long itemId) {
        return commentStorage.getCommentByItem(itemId);
    }

    @Override
    public Comment addComment(long itemId, long userId, String text) {
        if (isNullOrBlank(text))
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Комментарий не может быть пуст");
        Item item = getById(itemId);
        List<Booking> bookings = bookingStorage.getByBookerIdAndTime(userId, ReqStatus.PAST, false);
        if (bookings.isEmpty())
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Нельзя комментировать тому кто не брал вещь");

        Comment comment = new Comment(
                0,
                text,
                item,
                userStorage.get(userId),
                now());

        return commentStorage.save(comment);
    }

    private boolean isNullOrBlank(String str) {
        if (str == null) return true;
        return str.isBlank();
    }

    private Booking getNextItem(List<Booking> bookings) {
        if (bookings != null)
            return bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now()))
                    .min(comparing(Booking::getEnd))
                    .orElse(null);
        else
            return null;
    }

    private Booking getLastItem(List<Booking> bookings) {
        if (bookings != null) {
            Booking res = bookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(now()))
                    .max(comparing(Booking::getEnd))
                    .orElse(null);
            if (res == null) {
                res = bookings.stream()
                        .filter(booking -> booking.getStart().isBefore(now()))
                        .max(comparing(Booking::getStart))
                        .orElse(null);
            }
            return res;
        } else
            return null;
    }
}
