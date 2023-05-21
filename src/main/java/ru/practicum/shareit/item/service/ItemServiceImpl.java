package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.logger.ShareitLogger;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

/**
 * @author Stanislav Makarov
 */
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemStorage itemStorage;
    @Autowired
    UserStorage userStorage;

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
        return itemStorage.getById(itemId);
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        return itemStorage.getAllUserItems(userId);
    }

    @Override
    public List<Item> findByText(String text) {
        return itemStorage.findByText(text);
    }

    private boolean isNullOrBlank(String str) {
        if (str == null) return true;
        return str.isBlank();
    }
}
