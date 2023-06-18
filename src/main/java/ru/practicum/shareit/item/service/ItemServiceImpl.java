package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.ReqStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentStorage;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.dto.ItemOutDto2;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utilites.ShareitHelper;

import javax.transaction.Transactional;
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
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private BookingStorage bookingStorage;
    @Autowired
    private CommentStorage commentStorage;
    @Autowired
    private ItemRequestService requestService;

    @Override
    public Item create(ItemInDto itemInDto, long userId) {
        if (userId <= 0) ShareitHelper.returnErrorMsg(HttpStatus.INTERNAL_SERVER_ERROR,
                "Не указан пользователь");

        Item item = ItemMapper.toItem(itemInDto);

        if (isNullOrBlank(item.getName()))
            ShareitHelper.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не задано название вещи");
        if (isNullOrBlank(item.getDescription()))
            ShareitHelper.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не задано описание вещи");
        if (item.getAvailable() == null)
            ShareitHelper.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не указана доступность вещи");

        item.setOwner(userStorage.get(userId));
        if (itemInDto.getRequestId() != null) {
            ItemRequest itemRequest = requestService.getById(itemInDto.getRequestId(), userId);
            if (itemRequest == null)
                ShareitHelper.returnErrorMsg(HttpStatus.BAD_REQUEST, "Указан несуществующий запрос");
            item.setRequest(itemRequest);
        }

        return itemStorage.create(item);
    }

    @Override
    public Item update(long itemId, ItemInDto itemInDto, long userId) {
        Item existItem = itemStorage.getById(itemId);
        if (existItem == null)
            ShareitHelper.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID вещи %d не найден", itemId));
        if (existItem.getOwner().getId() != userId)
            ShareitHelper.returnErrorMsg(HttpStatus.FORBIDDEN, "Редактировать вещь может только её владелец.");
        Item item = ItemMapper.toItem(itemInDto);
        return itemStorage.update(itemId, item);
    }

    @Override
    public Item getById(long itemId) {
        Item existItem = itemStorage.getById(itemId);
        if (existItem == null)
            ShareitHelper.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID вещи %d не найден", itemId));

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
            itemOutDto2.setLastBooking(BookingMapper.toBookingOutDto2(getLastItem(bookings)));
            itemOutDto2.setNextBooking(BookingMapper.toBookingOutDto2(getNextItem(bookings)));
        }
        return itemOutDto2;
    }

    @Override
    public List<ItemOutDto2> getAllUserItems(long userId) {
        List<Item> items = itemStorage.getAllUserItems(userId);
        List<ItemOutDto2> outDto2List = new ArrayList<>();

        List<Comment> allComments = commentStorage.getAllCommentsByItemList(items);
        List<Booking> allBookings = bookingStorage.getAllBookingsByItemList(items);

        for (Item item : items) {
            List<Comment> comments = allComments.stream().filter(c -> c.getItem().getId() == item.getId())
                    .collect(Collectors.toList());
            ItemOutDto2 itemOutDto2 = ItemMapper.toItemOutDto2(item, comments);
            List<Booking> bookings = allBookings.stream().filter(b -> b.getItem().getId() == item.getId())
                    .collect(Collectors.toList());

            bookings = bookings.stream().filter(b -> {
                BookingStatus status = b.getStatus();
                return status != BookingStatus.CANCELED &&
                        status != BookingStatus.REJECTED;
            }).collect(Collectors.toList());
            itemOutDto2.setLastBooking(BookingMapper.toBookingOutDto2(getLastItem(bookings)));
            itemOutDto2.setNextBooking(BookingMapper.toBookingOutDto2(getNextItem(bookings)));
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
            ShareitHelper.returnErrorMsg(HttpStatus.BAD_REQUEST, "Комментарий не может быть пуст");
        Item item = getById(itemId);
        List<Booking> bookings = bookingStorage.getByBookerIdAndTime(userId, ReqStatus.PAST, false, Pageable.unpaged());
        if (bookings.isEmpty())
            ShareitHelper.returnErrorMsg(HttpStatus.BAD_REQUEST, "Нельзя комментировать тому кто не брал вещь");

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
