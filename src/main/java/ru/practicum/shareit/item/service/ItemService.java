package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author Stanislav Makarov
 */
public interface ItemService {
    Item create(Item item, long userId);

    Item update(long itemId, Item item, long userId);

    Item getById(long itemId);

    List<Item> getAllUserItems(long userId);

    List<Item> findByText(String text);
}
