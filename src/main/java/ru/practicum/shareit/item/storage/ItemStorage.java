package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author Stanislav Makarov
 */
public interface ItemStorage {
    Item create(Item item);

    Item update(long itemId, Item item);

    Item getById(long itemId);

    List<Item> getAllUserItems(long userId);

    List<Item> findByText(String text);

    boolean isItemAvailable(long itemId);
}
