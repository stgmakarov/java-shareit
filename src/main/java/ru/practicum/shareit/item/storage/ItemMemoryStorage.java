package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stanislav Makarov
 */
@Component
@Slf4j
public class ItemMemoryStorage implements ItemStorage {
    private long lastItemNumber;
    private final Map<Long, Item> itemMap = new HashMap<>();

    private synchronized long getNewItemNumber() {
        return ++lastItemNumber;
    }

    @Override
    public Item create(Item item) {
        long newItemId = getNewItemNumber();
        item.setId(newItemId);
        itemMap.put(newItemId, item);
        return item;
    }

    @Override
    public Item update(long itemId, Item item) {
        Item existsItem = getById(itemId);
        if (item.getName() != null) existsItem.setName(item.getName());
        if (item.getDescription() != null) existsItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) existsItem.setAvailable(item.getAvailable());
        itemMap.put(itemId, existsItem);
        return existsItem;
    }

    @Override
    public Item getById(long itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Item> findByText(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemMap.values().stream()
                .filter(item -> (item.getAvailable()) &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toUnmodifiableList());
    }
}
