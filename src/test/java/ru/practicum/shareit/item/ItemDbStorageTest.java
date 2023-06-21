package ru.practicum.shareit.item;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemDbStorageTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemDbStorage itemDbStorage;

    @Test
    public void testCreate() {
        Item item = new Item();
        item.setId(1L);
        item.setOwner(new User(1L, "name", "user@mail.com"));

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemDbStorage.create(item);

        assertEquals(result.getId(), item.getId());
        assertEquals(result.getOwner().getId(), item.getOwner().getId());
    }

    @Test
    public void testUpdate() {
        Item item = new Item();
        item.setId(1L);
        item.setOwner(new User(1L, "name", "user@mail.com"));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Item createdItem = itemDbStorage.create(item);
        createdItem.setAvailable(true);
        when(itemRepository.save(createdItem)).thenReturn(createdItem);
        Item updatedItem = itemDbStorage.update(1L, createdItem);
        assertEquals(createdItem.getAvailable(), updatedItem.getAvailable());
    }

    @Test
    public void getByIdTest() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setId(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Item foundItem = itemDbStorage.getById(itemId);
        assertEquals(foundItem.getId(), item.getId());
    }

    @Test
    public void getByIdFail1Test() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setId(itemId);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemDbStorage.getById(itemId);
        });
    }

    @Test
    public void getAllUserItemsTest() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setId(itemId);
        when(itemRepository.findAllByOwner_IdOrderById(anyLong()))
                .thenReturn(Collections.singletonList(item));

        List<Item> res = itemDbStorage.getAllUserItems(1L);
        Assertions.assertNotNull(res);
        Assertions.assertEquals(res.get(0).getId(), item.getId());
    }

    @Test
    public void findByTextTest() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setId(itemId);
        when(itemRepository.findByDescriptionLikeIgnoreCaseAndAvailableEquals(anyString(), anyBoolean()))
                .thenReturn(Collections.singletonList(item));

        List<Item> res = itemDbStorage.findByText("test");
        Assertions.assertNotNull(res);
        Assertions.assertEquals(res.get(0).getId(), item.getId());
    }

    @Test
    public void isItemAvailableTest() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setId(itemId);
        when(itemRepository.existsByIdAndAvailable(anyLong(), anyBoolean()))
                .thenReturn(true);

        boolean res = itemDbStorage.isItemAvailable(1L);
        Assertions.assertTrue(res);
    }

}

