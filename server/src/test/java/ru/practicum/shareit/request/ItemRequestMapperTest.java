package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto2;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestMapperTest {

    @Test
    public void testToItemRequestOutDto() {
        List<Item> items = Arrays.asList(new Item(), new Item());
        User user = new User(1L, "John", "john@example.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now(), items);
        ItemRequestOutDto itemRequestOutDto = ItemRequestMapper.toItemRequestOutDto(itemRequest);
        assertNotNull(itemRequestOutDto);
        assertEquals(itemRequest.getId(), itemRequestOutDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestOutDto.getDescription());
        assertEquals(itemRequest.getRequestor().getName(), itemRequestOutDto.getRequestor().getName());
        assertEquals(itemRequest.getCreated(), itemRequestOutDto.getCreated());
        assertEquals(itemRequest.getItems().size(), itemRequestOutDto.getItems().size());
    }

    @Test
    public void testToItemRequestOutDtoWithNullItemRequest() {
        ItemRequest itemRequest = null;
        ItemRequestOutDto itemRequestOutDto = ItemRequestMapper.toItemRequestOutDto(itemRequest);
        assertNull(itemRequestOutDto);
    }

    @Test
    public void testToItemRequestOutDto2() {
        List<Item> items = Arrays.asList(new Item(), new Item());
        User user = new User(1L, "John", "john@example.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now(), items);
        ItemRequestOutDto2 itemRequestOutDto2 = ItemRequestMapper.toItemRequestOutDto2(itemRequest);
        assertNotNull(itemRequestOutDto2);
        assertEquals(itemRequest.getId(), itemRequestOutDto2.getId());
        assertEquals(itemRequest.getDescription(), itemRequestOutDto2.getDescription());
        assertEquals(itemRequest.getRequestor().getName(), itemRequestOutDto2.getRequestor().getName());
        assertEquals(itemRequest.getCreated(), itemRequestOutDto2.getCreated());
        assertEquals(itemRequest.getItems().size(), itemRequestOutDto2.getItems().size());
    }
}

