package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.dto.ItemOutDto2;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {
    @Test
    public void testToItemOutDto() {
        Item item = new Item(1L, "name", "description", true, new User(), null);
        ItemOutDto itemOutDto = ItemMapper.toItemOutDto(item);
        assertNotNull(itemOutDto);
        assertEquals(item.getId(), itemOutDto.getId());
        assertEquals(item.getName(), itemOutDto.getName());
        assertEquals(item.getDescription(), itemOutDto.getDescription());
        assertEquals(item.getAvailable(), itemOutDto.getAvailable());
        assertEquals(item.getOwner(), itemOutDto.getOwner());
        assertNull(itemOutDto.getRequestId());
    }

    @Test
    public void testToItemOutDto2() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, "text1", new Item(), new User(), LocalDateTime.now()));
        comments.add(new Comment(2L, "text2", new Item(), new User(), LocalDateTime.now()));
        Item item = new Item(1L, "name", "description", true, new User(), null);
        ItemOutDto2 itemOutDto2 = ItemMapper.toItemOutDto2(item, comments);
        assertNotNull(itemOutDto2);
        assertEquals(item.getId(), itemOutDto2.getId());
        assertEquals(item.getName(), itemOutDto2.getName());
        assertEquals(item.getDescription(), itemOutDto2.getDescription());
        assertEquals(item.getAvailable(), itemOutDto2.getAvailable());
        assertEquals(item.getOwner(), itemOutDto2.getOwner());
        assertNull(itemOutDto2.getRequestId());
        assertNotNull(itemOutDto2.getComments());
    }

    @Test
    public void testToItem() {
        ItemInDto itemInDto = new ItemInDto("name", "description", true, null);
        Item item = ItemMapper.toItem(itemInDto);
        assertNotNull(item);
        assertEquals(0L, item.getId());
        assertEquals(itemInDto.getName(), item.getName());
        assertEquals(itemInDto.getDescription(), item.getDescription());
        assertEquals(itemInDto.getAvailable(), item.getAvailable());
    }
}

