package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utilites.ShareitLogger;

import java.util.List;

@Primary
@Component
public class ItemDbStorage implements ItemStorage {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item create(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item update(long itemId, Item item) {
        Item existsItem = getById(itemId);
        if (item.getName() != null) existsItem.setName(item.getName());
        if (item.getDescription() != null) existsItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) existsItem.setAvailable(item.getAvailable());
        return itemRepository.save(existsItem);
    }

    @Override
    public Item getById(long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) ShareitLogger.
                returnErrorMsg(HttpStatus.NOT_FOUND, String.format("Вещь с ИД %d не найдена", itemId));
        return item;
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        return itemRepository.findAllByOwner_IdOrderById(userId);
    }

    @Override
    public List<Item> findByText(String text) {
        return itemRepository.findByDescriptionLikeIgnoreCaseAndAvailableEquals("%" + text + "%", true);
    }

    @Override
    public boolean isItemAvailable(long itemId) {
        return itemRepository.existsByIdAndAvailable(itemId, true);
    }
}
