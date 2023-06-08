package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.ReqStatus;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utilites.ParamNotFoundException;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utilites.ShareitLogger.returnErrorMsg;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingStorage bookingDbStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ItemService itemService;

    @Override
    public Booking create(BookingInDto booking, long userId) {
        checkUser(userId);
        if (!checkDate(booking))
            returnErrorMsg(HttpStatus.BAD_REQUEST, "Дата начала позже даты окончания бронирования");
        if (itemService.getById(booking.getItemId()) == null)
            returnErrorMsg(HttpStatus.NOT_FOUND, "Вещь не найдена");
        if (!itemService.isItemAvailable(booking.getItemId()))
            returnErrorMsg(HttpStatus.BAD_REQUEST, "Вещь недоступна для бронирования");
        if (itemService.getById(booking.getItemId()).getOwner().getId() == userId)
            returnErrorMsg(HttpStatus.NOT_FOUND, "Пользователь=владелец");
        return bookingDbStorage.create(
                BookingMapper.toBooking(booking,
                        userStorage.get(userId),
                        itemService.getById(booking.getItemId())));
    }

    @Override
    public Booking setApprove(long bookingId, long userId, boolean approved) {
        checkUser(userId);
        Booking existsBooking = bookingDbStorage.getById(bookingId);
        if (existsBooking == null)
            returnErrorMsg(HttpStatus.NOT_FOUND, "Бронирование не найдено");
        if (existsBooking.getItem().getOwner().getId() != userId)
            returnErrorMsg(HttpStatus.NOT_FOUND, "Подтверждать бронь может только владелец");
        if (existsBooking.getStatus() == BookingStatus.APPROVED)
            returnErrorMsg(HttpStatus.BAD_REQUEST, "Нельзя менять статус после подтверждения");
        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        existsBooking.setStatus(status);
        return bookingDbStorage.update(existsBooking);
    }

    @Override
    public Booking getBooking(long bookingId, long userId) {
        checkUser(userId);
        Booking existsBooking = bookingDbStorage.getById(bookingId);
        if (existsBooking == null)
            returnErrorMsg(HttpStatus.NOT_FOUND, "Бронирование не найдено");
        if (existsBooking.getItem().getOwner().getId() != userId && existsBooking.getBooker().getId() != userId)
            returnErrorMsg(HttpStatus.NOT_FOUND, "Просматривать бронь может владелец или бронирующий");

        return existsBooking;
    }

    @Override
    public List<Booking> getAllBooking(long userId, String status, boolean byOwner) throws ParamNotFoundException {
/*        Получение списка всех бронирований текущего пользователя.
        Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
        Также он может принимать значения CURRENT (англ. «текущие»),
        PAST (англ. «завершённые»),
        FUTURE (англ. «будущие»),
        WAITING (англ. «ожидающие подтверждения»),
        REJECTED (англ. «отклонённые»).

        Бронирования должны возвращаться отсортированными по дате от более новых к более старым.*/
        List<Booking> ret = null;
        ReqStatus reqStatus;
        try {
            reqStatus = ReqStatus.valueOf(status);
        } catch (Exception e) {
            throw new ParamNotFoundException("Unknown state: " + status);
        }
        checkUser(userId);
        switch (reqStatus) {
            case ALL:
                ret = bookingDbStorage.getByBookerId(userId, byOwner);
                break;
            case PAST:
            case FUTURE:
            case CURRENT:
                ret = bookingDbStorage.getByBookerIdAndTime(userId, reqStatus, byOwner);
                break;
            case WAITING:
            case REJECTED:
                ret = bookingDbStorage.getByBookerIdAndStatus(userId, reqStatus, byOwner);
                break;
        }

        return sortByDate(ret);
    }

    private List<Booking> sortByDate(List<Booking> lst) {
        List<Booking> ret = lst.stream().sorted(Comparator.comparing(Booking::getStart).reversed())
                .collect(Collectors.toList());
        return ret;
    }

    private boolean checkDate(BookingInDto booking) {
        return booking.getStart().isBefore(booking.getEnd());
    }

    private void checkUser(long userId) {
        if (userStorage.get(userId) == null)
            returnErrorMsg(HttpStatus.NOT_FOUND, "Пользователь не найден");
    }
}
