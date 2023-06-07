package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.dto.ItemOutDto2;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author Stanislav Makarov
 */
@UtilityClass
public class ItemMapper {
    public static ItemOutDto toItemOutDto(Item item) {
        return new ItemOutDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemOutDto2 toItemOutDto2(Item item, List<Comment> comments) {
        return new ItemOutDto2(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                null,
                null,
                CommentMapper.toCommentDto(comments)
        );
    }

    public static Item toItem(ItemInDto itemInDto) {
        return new Item(
                0,
                itemInDto.getName(),
                itemInDto.getDescription(),
                itemInDto.getAvailable(),
                null,
                null);
    }
}
