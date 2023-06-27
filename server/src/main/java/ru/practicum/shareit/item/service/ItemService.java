package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.dto.ItemOutDto2;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author Stanislav Makarov
 */
public interface ItemService {
    Item create(ItemInDto itemInDto, long userId);

    Item update(long itemId, ItemInDto itemInDto, long userId);

    Item getById(long itemId);

    ItemOutDto2 getByIdDto2(long itemId, long userId);

    List<ItemOutDto2> getAllUserItems(long userId);

    List<Item> findByText(String text);

    boolean isItemAvailable(long itemId);

    List<Comment> getItemComment(long itemId);

    Comment addComment(long itemId, long userId, String text);
}
