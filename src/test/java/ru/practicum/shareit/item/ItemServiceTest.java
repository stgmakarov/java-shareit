package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.storage.BookingDbStorage;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.comment.storage.CommentDbStorage;
import ru.practicum.shareit.comment.storage.CommentStorage;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    UserDbStorage userStorage;
    ItemServiceImpl itemService;
    ItemDbStorage itemStorage;
    BookingStorage bookingStorage;
    CommentStorage commentStorage;
    ItemRequestService requestService;

    User owner;
    User user;
    Item item;

    @BeforeEach
    public void init() {
        userStorage = Mockito.mock(UserDbStorage.class);
        itemStorage = Mockito.mock(ItemDbStorage.class);
        bookingStorage = Mockito.mock(BookingDbStorage.class);
        commentStorage = Mockito.mock(CommentDbStorage.class);
        requestService = Mockito.mock(ItemRequestService.class);

        itemService = new ItemServiceImpl(itemStorage,
                userStorage,
                bookingStorage,
                commentStorage,
                requestService);

        owner = new User(1L, "Owner name", "owner@mail.com");
        user = new User(3L, "User name", "user@mail.com");

        item = new Item(1L, "item", "description",
                true, owner, null);
    }

    @Test
    public void createItemTest() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(requestService.getById(anyLong(), anyLong())).thenReturn(new ItemRequest());

        when(itemStorage.create(any(Item.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Item item1 = itemService.create(
                new ItemInDto(
                        "name", "desc", true, 1L),
                user.getId());

        Assertions.assertNotNull(item1);
        Assertions.assertEquals(item1.getOwner().getId(), owner.getId());
    }
}
